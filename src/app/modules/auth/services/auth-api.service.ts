import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { LoginRequest, RegisterRequest, ForgotPasswordRequest, VerifyOtpRequest } from '../../../core/models/auth-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  constructor(private apiService: ApiService) {}

  login(loginRequest: LoginRequest): Observable<any> {
    return this.apiService.post('/auth/login', loginRequest);
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.apiService.post('/auth/register', registerRequest);
  }

  forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Observable<any> {
    return this.apiService.post('/auth/forgot-password', forgotPasswordRequest);
  }

  verifyOtp(verifyOtpRequest: VerifyOtpRequest): Observable<any> {
    return this.apiService.post('/auth/verify-otp', verifyOtpRequest);
  }

  resetPassword(resetPasswordRequest: any): Observable<any> {
    return this.apiService.post('/auth/reset-password', resetPasswordRequest);
  }

  refreshToken(): Observable<any> {
    return this.apiService.post('/auth/refresh-token', {});
  }

  logout(): Observable<any> {
    return this.apiService.post('/auth/logout', {});
  }
}
