import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
  imports: [ReactiveFormsModule, CommonModule]
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';
  token = '';
  isTokenValid = false;
  tokenValidated = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, {
      validator: this.mustMatch('newPassword', 'confirmPassword')
    });

    // Get token from URL parameters
    this.token = this.route.snapshot.queryParams['token'] || this.route.snapshot.paramMap.get('token') || '';
    
    if (this.token) {
      this.validateToken();
    } else {
      this.error = 'Invalid reset link. Please request a new password reset.';
    }
  }

  get f() { return this.resetPasswordForm.controls; }

  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
        return;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    };
  }

  validateToken(): void {
    this.authService.validateResetToken(this.token).subscribe({
      next: (response) => {
        this.isTokenValid = response.valid;
        this.tokenValidated = true;
        if (!this.isTokenValid) {
          this.error = 'This reset link has expired or is invalid. Please request a new password reset.';
        }
      },
      error: (error) => {
        this.error = 'Invalid reset token. Please request a new password reset.';
        this.tokenValidated = true;
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.resetPasswordForm.invalid || !this.isTokenValid) {
      return;
    }

    this.loading = true;

    this.authService.resetPassword(this.token, this.f['newPassword'].value).subscribe({
      next: () => {
        this.success = 'Password reset successfully! Redirecting to login...';
        this.loading = false;
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      },
      error: (error) => {
        this.error = error.message || 'Failed to reset password. Please try again.';
        this.loading = false;
      }
    });
  }
}
