import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { VerifyOtpRequest } from '../../../../core/models/auth-request.model';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-verify-otp',
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.scss'],
  imports: [ReactiveFormsModule, CommonModule]
})
export class VerifyOtpComponent implements OnInit {
  verifyOtpForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  email = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParamMap.get('email') || '';
    
    this.verifyOtpForm = this.formBuilder.group({
      otp: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]],
      email: [this.email, Validators.required]
    });
  }

  get f() { return this.verifyOtpForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.verifyOtpForm.invalid) {
      return;
    }

    this.loading = true;
    const verifyOtpRequest: VerifyOtpRequest = {
      email: this.f['email'].value,
      otp: this.f['otp'].value
    };

    this.authService.verifyOtp(verifyOtpRequest).subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        this.error = error.message;
        this.loading = false;
      }
    });
  }

  resendOtp(): void {
    // Implement resend OTP logic
    console.log('Resend OTP for:', this.email);
  }
}
