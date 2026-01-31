# Password Reset API Postman Collection

This Postman collection is designed to test the password reset functionality of the EMS Backend application.

## Import Instructions

1. Open Postman
2. Click on **Import** in the top left
3. Select the `Password-Reset-API.postman_collection.json` file
4. The collection will be imported with all requests and variables

## Environment Variables

The collection includes the following environment variables:

- `baseUrl`: Default is `http://localhost:8080` - Update if your backend runs on a different port
- `resetToken`: Leave empty initially - You'll need to set this with a valid token from your database or email

## Testing Workflow

### 1. Start Your Backend
Make sure your EMS Backend application is running on the specified port (default: 8080).

### 2. Test the Complete Flow

#### Step 1: Forgot Password
- **Request**: `1. Forgot Password`
- **Method**: POST
- **Endpoint**: `/api/auth/forgot-password`
- **Body**: 
```json
{
    "email": "user@example.com"
}
```
- **Expected Response**: 
```json
{
    "message": "Password reset link sent to your email"
}
```

#### Step 2: Get Reset Token
Since email sending might not be configured in development, you'll need to get the reset token directly from your database:

```sql
SELECT token FROM password_reset_tokens WHERE user_id = [your_user_id] ORDER BY created_at DESC LIMIT 1;
```

Copy this token and set it in the `resetToken` environment variable in Postman.

#### Step 3: Validate Reset Token (Optional)
- **Request**: `2. Validate Reset Token`
- **Method**: GET
- **Endpoint**: `/api/auth/validate-reset-token?token={{resetToken}}`
- **Expected Response**: 
```json
{
    "message": "Token is valid"
}
```

#### Step 4: Reset Password
- **Request**: `3. Reset Password`
- **Method**: POST
- **Endpoint**: `/api/auth/reset-password`
- **Body**:
```json
{
    "token": "{{resetToken}}",
    "newPassword": "NewSecurePassword123!",
    "confirmPassword": "NewSecurePassword123!"
}
```
- **Expected Response**:
```json
{
    "message": "Password reset successfully"
}
```

### 3. Test Error Cases

The collection also includes error case tests:

#### Test Invalid Email
- **Request**: `4. Test Invalid Email`
- Tests the forgot password endpoint with a non-existent email
- **Expected**: 400 Bad Request

#### Test Invalid Token
- **Request**: `5. Test Invalid Token`
- Tests password reset with an invalid token
- **Expected**: 400 Bad Request

#### Test Password Mismatch
- **Request**: `6. Test Password Mismatch`
- Tests password reset with non-matching passwords
- **Expected**: 400 Bad Request

## Important Notes

1. **Email Configuration**: In development, you might not have email configured. You'll need to manually get the reset token from the database.

2. **Token Expiration**: Default token expiration is 15 minutes. Make sure to test within this timeframe.

3. **Database Cleanup**: Consider manually cleaning up expired tokens from the `password_reset_tokens` table after testing.

4. **User Account**: Ensure you have a test user account in your database with the email you're testing.

## Security Considerations

- The reset tokens are single-use and will be marked as used after successful password reset
- All previous tokens for a user are invalidated when a new reset is requested
- Tokens expire after the configured time (default: 15 minutes)

## Troubleshooting

### Common Issues

1. **404 Not Found**: Check if your backend is running and the `baseUrl` is correct
2. **400 Bad Request**: Verify the request body format and required fields
3. **Token Invalid**: Ensure the token is not expired, used, or manually entered correctly
4. **User Not Found**: Make sure the user email exists in your database

### Database Queries for Testing

```sql
-- Check if user exists
SELECT * FROM users WHERE email = 'user@example.com';

-- Get latest reset token for a user
SELECT * FROM password_reset_tokens 
WHERE user_id = [user_id] 
ORDER BY created_at DESC 
LIMIT 1;

-- Clean up test tokens
DELETE FROM password_reset_tokens WHERE created_at < NOW() - INTERVAL '1 hour';
```

## Customization

You can modify the collection to:
- Add more test cases
- Change the default test data
- Add authentication headers if required
- Include additional validation scripts
