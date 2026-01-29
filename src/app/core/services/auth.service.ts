import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { LoginRequest, RegisterRequest, ForgotPasswordRequest, VerifyOtpRequest } from '../models/auth-request.model';
import { TokenStorageService } from './token-storage.service';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private tokenStorage: TokenStorageService,
    private apiService: ApiService,
    private router: Router
  ) {
    this.loadUserFromToken();
  }

  login(loginRequest: LoginRequest): Observable<any> {
    return this.apiService.post('/auth/login', loginRequest);
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.apiService.post('/auth/register', registerRequest);
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<any> {
    return this.apiService.post('/auth/forgot-password', request);
  }

  verifyOtp(request: VerifyOtpRequest): Observable<any> {
    return this.apiService.post('/auth/verify-otp', request);
  }

  logout(): void {
    this.tokenStorage.signOut();
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getUserRole(): string | null {
    const user = this.getCurrentUser();
    return user?.roles[0]?.name || null;
  }

  private loadUserFromToken(): void {
    const token = this.tokenStorage.getToken();
    if (token) {
      // Decode token to get user info or make API call
      // This is a simplified version
      this.currentUserSubject.next(null);
    }
  }
}
