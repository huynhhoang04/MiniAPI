# Mini Job Board API

Mini project tuyển dụng: quản lý **Company**, **Job** và **Application** bằng REST API.

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL 16**
- **Maven**
- **Flyway** (quản lý schema bằng migration)
- **Spring Data JPA**

---

## 1. Yêu cầu môi trường

| Công cụ | Phiên bản |
|---|---|
| JDK | 17+ |
| Maven | 3.8+ |
| Docker & Docker Compose | (để chạy PostgreSQL) |

Kiểm tra nhanh:

```bash
java -version
mvn -version
docker --version
```

---

## 2. Chạy project

### Bước 1 — Khởi động PostgreSQL

```bash
docker compose up -d
```

Cấu hình mặc định trong `docker-compose.yml`:

| Thông số | Giá trị |
|---|---|
| Host | `localhost` |
| Port | `5432` |
| Database | `jobboard` |
| Username | `jobboard` |
| Password | `jobboard` |

### Bước 2 — Chạy ứng dụng

```bash
mvn spring-boot:run
```

Ứng dụng chạy tại: `http://localhost:8080`

> Khi khởi động, **Flyway** tự động chạy migration trong
> `src/main/resources/db/migration/` để tạo bảng, index và constraint.
> Không cần tạo bảng thủ công.

### Bước 3 — (Tùy chọn) Build file jar

```bash
mvn clean package
java -jar target/job-board-0.0.1-SNAPSHOT.jar
```

### Dừng database

```bash
docker compose down
```

---

## 3. Cấu trúc project

```
src/main/java/com/rikkei/jobboard
├── JobBoardApplication.java
├── common/
│   ├── exception/          # Ngoại lệ nghiệp vụ + ErrorResponse
│   └── GlobalExceptionHandler.java
├── company/                # Company entity, repository, service, controller, dto
├── job/                    # Job entity, status, repository, service, controller, dto, specification
└── application/            # Application entity, repository, service, controller, dto

src/main/resources
├── application.yml
└── db/migration/V1__init.sql   # Script tạo bảng
```

Thiết kế theo **feature-based package**: mỗi domain tự đóng gói
entity/repository/service/controller/dto, quan hệ JPA một chiều (`@ManyToOne` LAZY),
dữ liệu trả ra ngoài luôn qua **DTO**, không trả entity trực tiếp.

---

## 4. Thiết kế cơ sở dữ liệu

### `company`

| Cột | Kiểu | Ghi chú |
|---|---|---|
| `id` | `BIGSERIAL` | PK |
| `name` | `VARCHAR(255)` | NOT NULL |
| `address` | `VARCHAR(255)` | |
| `website` | `VARCHAR(255)` | |
| `created_at` | `TIMESTAMP` | |
| `updated_at` | `TIMESTAMP` | |

### `job`

| Cột | Kiểu | Ghi chú |
|---|---|---|
| `id` | `BIGSERIAL` | PK |
| `company_id` | `BIGINT` | FK → `company(id)` |
| `title` | `VARCHAR(255)` | NOT NULL |
| `description` | `TEXT` | |
| `salary_min` | `NUMERIC(15,2)` | |
| `salary_max` | `NUMERIC(15,2)` | |
| `location` | `VARCHAR(255)` | |
| `status` | `VARCHAR(20)` | NOT NULL, `OPEN` / `CLOSED` |
| `created_at` | `TIMESTAMP` | |
| `updated_at` | `TIMESTAMP` | |

### `application`

| Cột | Kiểu | Ghi chú |
|---|---|---|
| `id` | `BIGSERIAL` | PK |
| `job_id` | `BIGINT` | FK → `job(id)` |
| `full_name` | `VARCHAR(255)` | NOT NULL |
| `email` | `VARCHAR(255)` | NOT NULL |
| `cv_url` | `VARCHAR(500)` | NOT NULL |
| `applied_at` | `TIMESTAMP` | NOT NULL, mặc định `now()` |

**Ràng buộc quan trọng**

- `UNIQUE (job_id, email)` — chặn trùng email trong cùng một Job ở tầng database
  (service kiểm tra trước để trả về 409, constraint xử lý trường hợp race condition).
- Index: `job(status)`, `job(location)`, `job(company_id)`, `application(job_id)`.

---

## 5. Danh sách API

> Base path: `/api`. Mọi response danh sách đều trả về dạng `Page` của Spring
> (`content`, `page`, `size`, `totalElements`, `totalPages`, ...).

### Company

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/companies` | Tạo company |
| `GET` | `/api/companies` | Danh sách (phân trang) |
| `GET` | `/api/companies/{id}` | Chi tiết |
| `PUT` | `/api/companies/{id}` | Cập nhật |
| `DELETE` | `/api/companies/{id}` | Xoá |

### Job

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/jobs` | Tạo job |
| `GET` | `/api/jobs` | Tìm kiếm + lọc + phân trang + sắp xếp |
| `GET` | `/api/jobs/{id}` | Chi tiết |
| `PUT` | `/api/jobs/{id}` | Cập nhật |
| `DELETE` | `/api/jobs/{id}` | Xoá |

**Tham số tìm kiếm của `GET /api/jobs`**

| Tham số | Kiểu | Mô tả |
|---|---|---|
| `keyword` | string | Tìm trong `title` và `description` (không phân biệt hoa/thường) |
| `location` | string | Lọc theo địa điểm |
| `status` | `OPEN` \| `CLOSED` | Lọc theo trạng thái |
| `page` | int | Trang, mặc định `0` |
| `size` | int | Số phần tử/trang, mặc định `10` |
| `sort` | string | `field,direction` — ví dụ `salaryMin,desc`. Hỗ trợ: `title`, `salaryMin`, `salaryMax`, `createdAt` |

### Application

| Method | Endpoint | Mô tả |
|---|---|---|
| `POST` | `/api/jobs/{jobId}/applications` | Nộp hồ sơ |
| `GET` | `/api/jobs/{jobId}/applications` | Danh sách hồ sơ của job |

**Quy tắc khi nộp hồ sơ**

- Job không tồn tại → `404 Not Found`
- Job đang `CLOSED` → `409 Conflict`
- Email đã nộp cho cùng Job → `409 Conflict`

---

## 6. Format lỗi thống nhất

Mọi lỗi trả về cùng một cấu trúc JSON:

```json
{
  "timestamp": "2026-11-04T10:15:30.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/jobs",
  "details": [
    { "field": "title", "message": "must not be blank" },
    { "field": "salaryMax", "message": "must be greater than or equal to salaryMin" }
  ]
}
```

| Mã | Trường hợp |
|---|---|
| `400` | Dữ liệu đầu vào không hợp lệ, JSON sai định dạng |
| `404` | Không tìm thấy Company / Job |
| `409` | Trùng email trong cùng Job, Job đã `CLOSED`, vi phạm ràng buộc duy nhất |

---

## 7. Ví dụ request

### Tạo company

```bash
curl -X POST http://localhost:8080/api/companies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rikkei Soft",
    "address": "Hà Nội",
    "website": "https://rikkeisoft.com"
  }'
```

### Tạo job

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "companyId": 1,
    "title": "Java Backend Developer",
    "description": "Phát triển REST API với Spring Boot",
    "salaryMin": 15000000,
    "salaryMax": 25000000,
    "location": "Hà Nội",
    "status": "OPEN"
  }'
```

### Tìm kiếm job

```bash
curl "http://localhost:8080/api/jobs?keyword=java&location=H%C3%A0%20N%E1%BB%99i&status=OPEN&page=0&size=10&sort=salaryMin,desc"
```

### Cập nhật job

```bash
curl -X PUT http://localhost:8080/api/jobs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "companyId": 1,
    "title": "Senior Java Backend Developer",
    "description": "Phát triển REST API với Spring Boot",
    "salaryMin": 20000000,
    "salaryMax": 35000000,
    "location": "Hà Nội",
    "status": "OPEN"
  }'
```

### Nộp hồ sơ

```bash
curl -X POST http://localhost:8080/api/jobs/1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Nguyễn Văn A",
    "email": "vana@example.com",
    "cvUrl": "https://drive.google.com/file/d/abc123/view"
  }'
```

### Nộp trùng email (trả về 409)

```bash
curl -X POST http://localhost:8080/api/jobs/1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Nguyễn Văn A",
    "email": "vana@example.com",
    "cvUrl": "https://drive.google.com/file/d/abc123/view"
  }'
```

```json
{
  "timestamp": "2026-11-04T10:20:00.000Z",
  "status": 409,
  "error": "Conflict",
  "message": "Email vana@example.com đã nộp hồ sơ cho job này",
  "path": "/api/jobs/1/applications"
}
```

### Danh sách hồ sơ của job

```bash
curl "http://localhost:8080/api/jobs/1/applications?page=0&size=10"
```

---

## 8. Kiểm dữ liệu đầu vào

| Trường | Ràng buộc |
|---|---|
| `Company.name` | Không rỗng, tối đa 255 ký tự |
| `Company.website` | Định dạng URL hợp lệ |
| `Job.title` | Không rỗng, tối đa 255 ký tự |
| `Job.companyId` | Bắt buộc, company phải tồn tại |
| `Job.salaryMin`, `Job.salaryMax` | Không âm; `salaryMin <= salaryMax` |
| `Job.status` | Chỉ nhận `OPEN` / `CLOSED` |
| `Application.fullName` | Không rỗng |
| `Application.email` | Đúng định dạng email |
| `Application.cvUrl` | Định dạng URL hợp lệ |

---

## 9. Chạy test

```bash
mvn test
```

Unit test tầng service (Mockito) bao gồm tối thiểu:

1. Nộp hồ sơ thành công
2. Nộp hồ sơ khi job không tồn tại → `404`
3. Nộp hồ sơ khi job `CLOSED` → `409`
4. Nộp hồ sơ trùng email trong cùng job → `409`
5. Tìm kiếm job trả về đúng kết quả phân trang
6. Tạo job khi company không tồn tại → `404`

---

## 10. Quy ước Git

- Commit nhỏ, mỗi commit một mục đích rõ ràng.
- Message theo dạng: `<scope>: <mô tả ngắn>`.

Ví dụ:

```
init: khởi tạo project Spring Boot
db: thêm entity và migration V1
feat: CRUD company
feat: CRUD job
feat: tìm kiếm job theo keyword, location, status
feat: nộp hồ sơ ứng tuyển
feat: xử lý lỗi và validation tập trung
test: unit test tầng service
docs: bổ sung README
```
