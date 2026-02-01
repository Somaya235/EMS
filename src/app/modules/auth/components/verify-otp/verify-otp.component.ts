import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { VerifyOtpRequest } from '../../../../core/models/auth-request.model';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-verify-otp',
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.css'],
  imports: [ReactiveFormsModule, CommonModule, RouterLink]
})
export class VerifyOtpComponent implements OnInit {
  verifyOtpForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';
  email = '';
  otpSent = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParamMap.get('email') || '';

    this.verifyOtpForm = this.formBuilder.group({
      otp: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]],
      email: [this.email, [Validators.required, Validators.email]]
    });
  }

  get f() { return this.verifyOtpForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.verifyOtpForm.invalid) {
      return;
    }

    this.loading = true;
    const verifyOtpRequest: VerifyOtpRequest = {
      email: this.f['email'].value,
      otp: this.f['otp'].value
    };

    this.authService.verifyOtp(verifyOtpRequest).subscribe({
      next: (response) => {
        this.success = 'Email verified successfully! Redirecting to login...';
        this.loading = false;
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      },
      error: (error) => {
        this.error = error.error?.message || 'OTP verification failed. Please try again.';
        this.loading = false;
      }
    });
  }

  resendOtp(): void {
    this.loading = true;
    this.error = '';
    this.success = '';

    this.authService.resendOtp(this.f['email'].value).subscribe({
      next: (response) => {
        this.success = 'OTP has been resent to your email.';
        this.loading = false;
        this.otpSent = true;
        setTimeout(() => {
          this.otpSent = false;
        }, 5000);
      },
      error: (error) => {
        this.error = error.error?.message || 'Failed to resend OTP. Please try again.';
        this.loading = false;
      }
    });
  }

  onOtpInput(event: any): void {
    // Allow only numbers and limit to 6 digits
    let value = event.target.value.replace(/[^0-9]/g, '');
    if (value.length > 6) {
      value = value.slice(0, 6);
    }
    this.f['otp'].setValue(value);
  }
}
