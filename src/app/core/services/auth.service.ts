import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { User } from '../models/user.model';
import { LoginRequest, RegisterRequest, ForgotPasswordRequest, VerifyOtpRequest, RefreshTokenRequest, JwtResponse } from '../models/auth-request.model';
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

  login(loginRequest: LoginRequest): Observable<JwtResponse> {
    return this.apiService.post<JwtResponse>('/auth/signin', loginRequest).pipe(
      map(response => {
        console.log('Login response:', response);

        // Handle both 'token' and 'accessToken' field names for compatibility
        const token = response.accessToken || (response as any).token;
        const refreshToken = response.refreshToken || (response as any).refreshToken;

        if (token) {
          // TODO: Implement HttpOnly cookies for better security
          // Current implementation uses sessionStorage (less secure)
          // For production, consider:
          // 1. HttpOnly cookies set by backend
          // 2. Secure flag (HTTPS only)
          // 3. SameSite policy

          this.tokenStorage.saveToken(token);
          if (refreshToken) {
            this.tokenStorage.saveRefreshToken(refreshToken);
          }

          // Store user info - handle both structured user object and individual fields
          let userData: User | null = null;

          if (response.user) {
            // Backend returns user object
            userData = response.user;
            console.log('Using user object from response:', userData);
          } else if (response.id && response.email && response.fullName) {
            // Backend returns individual user fields
            userData = {
              id: response.id,
              fullName: response.fullName,
              email: response.email,
              passwordHash: '', // Not returned in login response
              grade: '',
              phoneNumber: '',
              nationalNumber: '',
              dateOfBirth: new Date(),
              collageId: '',
              roles: response.roles || [],
              enabled: true
            };
            console.log('Created user object from individual fields:', userData);
          } else {
            console.warn('No user data found in login response');
          }

          if (userData) {
            this.tokenStorage.saveUser(userData);
            this.currentUserSubject.next(userData);
            console.log('User data saved and subject updated');
          }
        }
        return response;
      })
    );
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.apiService.post('/auth/signup', registerRequest);
  }

  verifyOtp(request: VerifyOtpRequest): Observable<any> {
    return this.apiService.post('/auth/verify-otp', request);
  }

  resendOtp(email: string): Observable<any> {
    return this.apiService.post(`/auth/resend-otp?email=${email}`, {});
  }

  refreshToken(refreshTokenRequest: RefreshTokenRequest): Observable<JwtResponse> {
    return this.apiService.post<JwtResponse>('/auth/refreshtoken', refreshTokenRequest).pipe(
      map(response => {
        // Handle both 'token' and 'accessToken' field names for compatibility
        const token = response.accessToken || (response as any).token;
        const refreshToken = response.refreshToken || (response as any).refreshToken;

        if (token) {
          this.tokenStorage.saveToken(token);
          if (refreshToken) {
            this.tokenStorage.saveRefreshToken(refreshToken);
          }
        }
        return response;
      })
    );
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<any> {
    return this.apiService.post('/auth/forgot-password', request);
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.apiService.post('/auth/reset-password', {
      token: token,
      newPassword: newPassword
    });
  }

  validateResetToken(token: string): Observable<any> {
    return this.apiService.get(`/auth/validate-reset-token?token=${token}`);
  }

  logout(): Observable<any> {
    return this.apiService.post('/auth/logout', {}).pipe(
      map(() => {
        this.tokenStorage.signOut();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
      }),
      catchError((error) => {
        // Even if logout API fails, clear local session
        this.tokenStorage.signOut();
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
        return of(null);
      })
    );
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getUserRole(): string | null {
    const user = this.getCurrentUser();
    return user?.roles?.[0] || null;
  }

  private loadUserFromToken(): void {
    const token = this.tokenStorage.getToken();
    const user = this.tokenStorage.getUser();

    if (token && user) {
      // Load user from token storage
      this.currentUserSubject.next(user);
    } else if (token) {
      // If token exists but no user data, make API call to get current user
      this.apiService.get<User>('/auth/me').pipe(
        catchError((error: any) => {
          console.error('Error loading current user:', error);
          this.currentUserSubject.next(null);
          return of(null);
        })
      ).subscribe((user: User | null) => {
        if (user) {
          this.tokenStorage.saveUser(user);
          this.currentUserSubject.next(user);
        } else {
          this.currentUserSubject.next(null);
        }
      });
    } else {
      this.currentUserSubject.next(null);
    }
  }
}
