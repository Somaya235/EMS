import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from '../../../../core/models/auth-request.model';
import { AuthService } from '../../../../core/services/auth.service';
import { SecureLoggerService } from '../../../../core/services/secure-logger.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, CommonModule, RouterLink]
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private secureLogger: SecureLoggerService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    const loginRequest: LoginRequest = {
      email: this.f['email'].value,
      password: this.f['password'].value
    };

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.secureLogger.log('Login successful', response);
        this.loading = false;

        // Add a small delay to ensure tokens are stored
        setTimeout(() => {
          // Verify user is logged in before navigation
          const isLoggedIn = this.authService.isLoggedIn();

          if (isLoggedIn) {
            // Navigate to dashboard after successful login
            this.router.navigate(['/dashboard']).then(
              (success) => {
                this.secureLogger.log('Navigation to dashboard successful', { success });
              },
              (error) => {
                this.secureLogger.error('Navigation to dashboard failed', error);
                this.error = 'Login successful but navigation failed. Please try again.';
              }
            );
          } else {
            this.secureLogger.error('User not logged in after login attempt');
            this.error = 'Login successful but authentication failed. Please try again.';
          }
        }, 100);
      },
      error: (error) => {
        this.secureLogger.error('Login failed', error);
        this.error = error.error?.message || 'Login failed. Please try again.';
        this.loading = false;
      }
    });
  }
}
