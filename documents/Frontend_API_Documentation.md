# EMS Frontend API Documentation

## Table of Contents
- [Authentication](#authentication)
- [Base URL](#base-url)
- [Common Headers](#common-headers)
- [Response Format](#response-format)
- [Error Handling](#error-handling)
- [API Endpoints](#api-endpoints)
  - [Authentication Endpoints](#authentication-endpoints)
  - [User Management](#user-management)
  - [Student Activities](#student-activities)
  - [Admin Student Activities](#admin-student-activities)
  - [Admin Student Management](#admin-student-management)
  - [Events](#events)
  - [Committees](#committees)
  - [Polls & Voting](#polls--voting)

---

## Authentication

The EMS API uses JWT (JSON Web Token) based authentication. Most endpoints require a valid JWT token to be included in the Authorization header.

### User Roles
- **super_admin**: Full system access, can create student activities and manage all users
- **activity_president**: Can manage their specific student activity, committees, and directors
- **committee_head**: Can manage committee members
- **authenticated_user**: Basic authenticated user access

---

## Base URL

```
http://localhost:8080
```

---

## Common Headers

### Required for Authenticated Requests
```http
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

### For File Uploads
```http
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data
```

---

## Response Format

### Success Response
```json
{
  "data": { ... },
  "message": "Success message",
  "status": "success"
}
```

### Error Response
```json
{
  "message": "Error description",
  "status": "error",
  "error": "Detailed error information"
}
```

---

## Error Handling

Common HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

---

## API Endpoints

### Authentication Endpoints

#### Sign In
```http
POST /api/auth/signin
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "jwt_token_here",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "roles": ["ROLE_USER"]
}
```

#### Sign Up
```http
POST /api/auth/signup
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "collageId": "COL12345",
  "grade": "Sophomore",
  "major": "Computer Science"
}
```

**Response:**
```json
{
  "message": "User registered successfully! Please check your email for OTP verification."
}
```

#### Verify OTP
```http
POST /api/auth/verify-otp
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

#### Resend OTP
```http
POST /api/auth/resend-otp?email=user@example.com
```

#### Refresh Token
```http
POST /api/auth/refreshtoken
```

**Request Body:**
```json
{
  "refreshToken": "refresh_token_here"
}
```

#### Logout
```http
POST /api/auth/logout
```

#### Forgot Password
```http
POST /api/auth/forgot-password
```

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

#### Reset Password
```http
POST /api/auth/reset-password
```

**Request Body:**
```json
{
  "token": "reset_token_here",
  "newPassword": "new_password123"
}
```

#### Validate Reset Token
```http
GET /api/auth/validate-reset-token?token=reset_token_here
```

---

### User Management

#### Get User Profile
```http
GET /api/users/profile/{id}
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Response:**
```json
{
  "id": 1,
  "username": "user@example.com",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": true,
  "grade": "Sophomore",
  "phoneNumber": "+1234567890",
  "nationalNumber": "123456789",
  "dateOfBirth": "2000-01-01",
  "collageId": "COL12345",
  "activities": [
    {
      "id": 1,
      "name": "Computer Club",
      "description": "Programming and tech activities",
      "category": "Academic"
    }
  ]
}
```

#### Update User Profile
```http
PUT /api/users/profile/{id}
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Request Body:**
```json
{
  "fullName": "John Smith",
  "grade": "Junior",
  "major": "Computer Science",
  "phoneNumber": "+1234567890",
  "nationalNumber": "123456789",
  "dateOfBirth": "2000-01-01",
  "collageId": "COL12345"
}
```

#### Register User (Internal)
```http
POST /api/users/register
```

#### Get User by Email (Internal)
```http
GET /api/users/{email}
```

---

### Student Activities

#### Get All Student Activities
```http
GET /api/student-activities
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Computer Club",
    "description": "Programming and tech activities",
    "category": "Academic",
    "president": {
      "id": 1,
      "fullName": "John Doe",
      "email": "john@example.com"
    },
    "directors": [
      {
        "id": 1,
        "director": {
          "id": 2,
          "fullName": "Jane Smith",
          "email": "jane@example.com"
        },
        "positionName": "Head of Logistics",
        "jobDescription": "Manages logistical aspects"
      }
    ]
  }
]
```

#### Get Student Activity by ID
```http
GET /api/student-activities/{id}
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Count All Student Activities
```http
GET /api/student-activities/count
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Response:**
```json
15
```

#### Get Directors for Student Activity
```http
GET /api/student-activities/{activityId}/directors
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Assign Activity Director (President Only)
```http
PUT /api/student-activities/{activityId}/directors
```

**Headers:** `Authorization: Bearer {president_jwt_token}`

**Request Body:**
```json
{
  "directorId": 2,
  "positionName": "Head of Logistics",
  "jobDescription": "Manages all logistical aspects for events and activities."
}
```

---

### Admin Student Activities

#### Create Student Activity with President (Super Admin Only)
```http
POST /api/admin/student-activities
```

**Headers:** `Authorization: Bearer {super_admin_jwt_token}`

**Request Body:**
```json
{
  "name": "Debate Club",
  "description": "Public speaking and debate activities",
  "category": "Academic",
  "presidentId": 3
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Student activity created and president assigned successfully",
  "activityId": 5,
  "presidentId": 3
}
```

#### Update Student Activity (Super Admin Only)
```http
PUT /api/admin/student-activities/{id}
```

**Headers:** `Authorization: Bearer {super_admin_jwt_token}`

**Request Body:**
```json
{
  "name": "Updated Activity Name",
  "description": "This is an updated description for the student activity.",
  "category": "Academic",
  "presidentId": 1
}
```

---

### Admin Student Management

#### Get All Students (Super Admin Only)
```http
GET /api/admin/students
```

**Headers:** `Authorization: Bearer {super_admin_jwt_token}`

**Response:**
```json
[
  {
    "id": 1,
    "fullName": "John Doe",
    "email": "john@example.com",
    "enabled": true,
    "grade": "Sophomore",
    "major": "Computer Science",
    "collageId": "COL12345",
    "roles": ["ROLE_USER"],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

---

### Events

#### Create Event
```http
POST /api/events
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Request Body:**
```json
{
  "title": "Tech Workshop",
  "description": "Learn modern web development",
  "date": "2024-03-15",
  "time": "14:00",
  "location": "Room 101",
  "activityId": 1
}
```

#### Get Event by ID
```http
GET /api/events/{id}
```

#### Get All Events
```http
GET /api/events
```

#### Update Event
```http
PUT /api/events/{id}
```

**Request Body:**
```json
{
  "title": "Updated Tech Workshop",
  "description": "Learn modern web development and frameworks",
  "date": "2024-03-20",
  "time": "15:00",
  "location": "Room 201",
  "activityId": 1
}
```

#### Delete Event
```http
DELETE /api/events/{id}
```

---

### Committees

#### Create Committee (Activity President Only)
```http
POST /api/committees
```

**Headers:** `Authorization: Bearer {activity_president_jwt_token}`

**Request Body:**
```json
{
  "name": "Event Planning Committee",
  "description": "Responsible for planning and organizing events",
  "activityId": 1
}
```

#### Assign Committee Head (Activity President Only)
```http
PUT /api/committees/{committeeId}/head
```

**Headers:** `Authorization: Bearer {activity_president_jwt_token}`

**Request Body:**
```json
5
```

#### Assign Committee Director (Activity President Only)
```http
PUT /api/committees/{committeeId}/director
```

**Headers:** `Authorization: Bearer {activity_president_jwt_token}`

**Request Body:**
```json
6
```

#### Get Committees by Activity
```http
GET /api/committees/activity/{activityId}
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Get Committee by ID
```http
GET /api/committees/{id}
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Get Committees by Director
```http
GET /api/committees/director/{directorId}
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Get Committee Members
```http
GET /api/committees/{committeeId}/members
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Count Committee Members
```http
GET /api/committees/{committeeId}/members/count
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Count Committees in Activity
```http
GET /api/committees/activity/{activityId}/count
```

**Headers:** `Authorization: Bearer {jwt_token}`

#### Add Member to Committee (Committee Head Only)
```http
POST /api/committees/{committeeId}/members
```

**Headers:** `Authorization: Bearer {committee_head_jwt_token}`

**Request Body:**
```json
{
  "userId": 7,
  "role": "Member"
}
```

#### Search Students (Committee Head Only)
```http
GET /api/committees/search-students?query=john
```

**Headers:** `Authorization: Bearer {committee_head_jwt_token}`

---

### Polls & Voting

#### Get Polls by Event
```http
GET /api/polls/event/{eventId}
```

**Headers:** `Authorization: Bearer {jwt_token}`

**Response:**
```json
[
  {
    "id": 1,
    "question": "What time should the event start?",
    "event": {
      "id": 1,
      "title": "Tech Workshop"
    },
    "options": [
      {
        "id": 1,
        "text": "2:00 PM",
        "votes": 5
      },
      {
        "id": 2,
        "text": "3:00 PM",
        "votes": 8
      }
    ],
    "createdAt": "2024-02-01T10:00:00"
  }
]
```

---

## Frontend Implementation Examples

### Angular HTTP Service Example

```typescript
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // Authentication
  signIn(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/signin`, credentials);
  }

  signUp(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/signup`, userData);
  }

  // Student Activities
  getAllActivities(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/student-activities`, {
      headers: this.getAuthHeaders()
    });
  }

  getActivityById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/student-activities/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // User Profile
  getUserProfile(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/users/profile/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  updateUserProfile(id: number, userData: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/users/profile/${id}`, userData, {
      headers: this.getAuthHeaders()
    });
  }
}
```

### Error Handling Example

```typescript
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

private handleError(error: any) {
  if (error.status === 401) {
    // Token expired or invalid
    localStorage.removeItem('jwt_token');
    // Redirect to login page
    this.router.navigate(['/login']);
  } else if (error.status === 403) {
    // Insufficient permissions
    console.error('Access forbidden');
  }
  return throwError(error);
}

// Usage with error handling
getAllActivities(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/student-activities`, {
    headers: this.getAuthHeaders()
  }).pipe(
    catchError(this.handleError.bind(this))
  );
}
```

---

## Testing with Postman

Use the provided Postman collections:
- `Student_Admin_Postman_Collection.json` - Admin endpoints
- `Student_Activities_Postman_Collection.json` - Student activity endpoints

### Environment Variables
```json
{
  "base_url": "http://localhost:8080",
  "jwt_token": "YOUR_AUTHENTICATED_JWT_TOKEN",
  "super_admin_jwt": "YOUR_SUPER_ADMIN_JWT_TOKEN",
  "president_jwt": "YOUR_ACTIVITY_PRESIDENT_JWT_TOKEN"
}
```

---

## Common Issues and Solutions

### 1. CORS Issues
Ensure the backend allows cross-origin requests from your frontend domain.

### 2. Token Expiration
Implement automatic token refresh using the refresh token endpoint.

### 3. Role-Based Access
Check user roles before accessing protected endpoints:
```typescript
const userRoles = JSON.parse(localStorage.getItem('user_roles') || '[]');
if (!userRoles.includes('ROLE_SUPER_ADMIN')) {
  // Redirect or show access denied
}
```

### 4. File Uploads
For endpoints that accept file uploads, use FormData:
```typescript
const formData = new FormData();
formData.append('file', file);
formData.append('otherField', 'value');

return this.http.post(url, formData, {
  headers: new HttpHeaders({
    'Authorization': `Bearer ${token}`
    // Don't set Content-Type - let browser set it with boundary
  })
});
```

---

## Security Considerations

1. **Token Storage**: Store JWT tokens securely (httpOnly cookies or secure storage)
2. **HTTPS**: Always use HTTPS in production
3. **Input Validation**: Validate all user inputs on both client and server side
4. **Role Validation**: Always verify user roles on the server side
5. **Token Refresh**: Implement proper token refresh mechanism

---

## Support

For API-related issues:
1. Check the backend logs for detailed error messages
2. Verify JWT token validity and expiration
3. Ensure proper user roles for protected endpoints
4. Test endpoints with Postman first before frontend integration
