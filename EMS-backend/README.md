# EMS - Event Management System Backend

A comprehensive Student Activity Event Management System built with Spring Boot, featuring complete authentication and authorization with JWT tokens, email OTP verification, and role-based access control.

## Features

### 🔐 Authentication & Authorization
- User Registration with Email OTP Verification
- JWT Access Tokens (15 minutes expiry)
- Refresh Tokens (7 days expiry)
- Password Encryption with BCrypt
- Role-based Access Control (RBAC)

### 📧 Email Services
- OTP Generation and Verification
- Registration Confirmation Emails
- Configurable SMTP Settings

### 👥 User Roles
- **super_admin**: Full system access
- **activity_president**: Manage student activities
- **web_manager**: System administration
- **activity_director**: Manage committees & events
- **committee_head**: Manage committee members & events
- **committee_member**: Committee participation
- **member**: View & vote in polls

## API Endpoints

### Authentication Endpoints

#### User Registration
```http
POST /api/auth/signup
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Email Verification
```http
POST /api/auth/verify-otp
Content-Type: application/json

{
  "email": "john@example.com",
  "otp": "123456"
}
```

#### Resend OTP
```http
POST /api/auth/resend-otp?email=john@example.com
```

#### User Login
```http
POST /api/auth/signin
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Refresh Token
```http
POST /api/auth/refreshtoken
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer <access-token>
```

### Protected Endpoints (Examples)

#### Public Access
```http
GET /api/test/all
```

#### User Profile (Authenticated Users)
```http
GET /api/test/profile
Authorization: Bearer <access-token>
```

#### Role-based Access
```http
GET /api/test/super-admin
Authorization: Bearer <access-token>
# Requires: super_admin role

GET /api/test/activity-president
Authorization: Bearer <access-token>
# Requires: activity_president role

GET /api/test/member
Authorization: Bearer <access-token>
# Requires: member role
```

## Configuration

### Environment Variables

Create a `.env` file or set environment variables:

```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/ems_db
DB_USER=postgres
DB_PASS=your_password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000

# Mail Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Server Configuration
SERVER_PORT=8080
LOG_LEVEL=INFO
SECURITY_LOG_LEVEL=INFO
```

### Database Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE ems_db;
```

2. The application will automatically create tables on startup using Hibernate DDL.

### Mail Configuration (Gmail)

1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account settings
   - Security → 2-Step Verification → App passwords
   - Generate a new app password
3. Use the app password in `MAIL_PASSWORD` environment variable

## Project Structure

```
src/main/java/com/example/EMS_backend/
├── config/
│   └── DataInitializer.java          # Initialize default roles
├── controllers/
│   ├── AuthController.java           # Authentication endpoints
│   └── TestController.java          # Example protected endpoints
├── dto/
│   ├── RegisterRequest.java          # Registration DTO
│   ├── LoginRequest.java            # Login DTO
│   ├── OtpRequest.java             # OTP verification DTO
│   ├── RefreshTokenRequest.java     # Refresh token DTO
│   ├── JwtResponse.java            # JWT response DTO
│   └── MessageResponse.java        # Generic message response
├── exceptions/
│   └── TokenRefreshException.java  # Custom exception
├── models/
│   ├── User.java                   # User entity
│   ├── Role.java                   # Role entity
│   ├── RefreshToken.java           # Refresh token entity
│   └── OtpCode.java               # OTP entity
├── repositories/
│   ├── UserRepository.java          # User repository
│   ├── RoleRepository.java          # Role repository
│   ├── RefreshTokenRepository.java  # Refresh token repository
│   └── OtpRepository.java          # OTP repository
├── security/
│   ├── JwtUtils.java               # JWT utilities
│   ├── UserDetailsImpl.java        # User details implementation
│   ├── UserDetailsServiceImpl.java  # User details service
│   ├── AuthTokenFilter.java        # JWT authentication filter
│   └── WebSecurityConfig.java      # Security configuration
└── services/
    ├── AuthService.java             # Authentication service
    ├── EmailService.java           # Email service
    └── RefreshTokenService.java    # Refresh token service
```

## Security Features

### JWT Token Flow
1. User logs in with email/password
2. System validates credentials
3. System generates JWT access token (15 min) + refresh token (7 days)
4. Client stores both tokens
5. Client sends access token in Authorization header for API calls
6. When access token expires, client uses refresh token to get new access token

### OTP Verification Flow
1. User registers with email/password
2. System generates 6-digit OTP (5 min expiry)
3. System sends OTP via email
4. User submits OTP for verification
5. System verifies OTP and enables account
6. User can now login

### Role-based Authorization
- Uses Spring Security's `@PreAuthorize` annotations
- Method-level security for fine-grained access control
- Multiple roles can be assigned to a user
- Hierarchical access patterns supported

## Running the Application

1. **Prerequisites:**
   - Java 22+
   - PostgreSQL 12+
   - Maven 3.6+

2. **Setup Database:**
   ```bash
   createdb ems_db
   ```

3. **Configure Environment:**
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/ems_db
   export DB_USER=postgres
   export DB_PASS=your_password
   export MAIL_USERNAME=your-email@gmail.com
   export MAIL_PASSWORD=your-app-password
   ```

4. **Run Application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access API:**
   - Base URL: `http://localhost:8080/api`
   - API will be available at the configured port

## Testing

### Test Registration and OTP Flow
```bash
# Register user
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"password123"}'

# Check email for OTP, then verify
curl -X POST http://localhost:8080/api/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","otp":"123456"}'
```

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

### Test Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/test/profile \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Development Notes

- All passwords are encrypted using BCrypt
- JWT tokens are signed with HS256 algorithm
- OTP codes are 6 digits, valid for 5 minutes
- Refresh tokens are stored in database for revocation support
- CORS is enabled for development (configure appropriately for production)
- Database schema is auto-generated by Hibernate (update mode)

## Production Considerations

1. **Security:**
   - Change default JWT secret key
   - Use environment variables for sensitive configuration
   - Enable HTTPS in production
   - Configure proper CORS origins

2. **Database:**
   - Use `validate` instead of `update` for DDL in production
   - Set up proper database backups
   - Configure connection pooling

3. **Email:**
   - Use production SMTP settings
   - Set up email templates
   - Configure rate limiting for OTP requests

4. **Monitoring:**
   - Enable application logging
   - Set up health checks
   - Monitor JWT token usage
