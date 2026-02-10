import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { RegisterRequest } from '../../../../core/models/auth-request.model';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [ReactiveFormsModule, CommonModule, RouterLink]
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';
  currentStep = 1;
  totalSteps = 3;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      fullName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), this.nameValidator]],
      email: ['', [Validators.required, Validators.email, this.emailValidator]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
      confirmPassword: ['', Validators.required],
      grade: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      phoneNumber: ['', [Validators.required, this.phoneNumberValidator]],
      nationalNumber: ['', [Validators.required, this.nationalNumberValidator]],
      dateOfBirth: ['', [Validators.required, this.ageValidator(16)]],
      collageId: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      roles: [['USER'], Validators.required]
    }, {
      validators: this.mustMatch('password', 'confirmPassword')
    });
  }

  get f() { return this.registerForm.controls; }

  // Enhanced validators
  nameValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    // Check for numbers and special characters
    const isValid = /^[a-zA-Z\s'-]+$/.test(value);
    return isValid ? null : { invalidName: true };
  }

  emailValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    // More comprehensive email validation
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const isValid = emailRegex.test(value);

    if (!isValid) return { invalidEmail: true };

    // Check for common typos
    const commonDomains = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com', 'edu'];
    const domain = value.split('@')[1]?.toLowerCase();

    if (domain && !commonDomains.some(cd => domain.includes(cd))) {
      return { uncommonDomain: true };
    }

    return null;
  }

  passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    let strength = 0;
    const errors: any = {};

    // Check length
    if (value.length >= 8) strength++;
    else errors.minLength = true;

    // Check for uppercase
    if (/[A-Z]/.test(value)) strength++;
    else errors.noUppercase = true;

    // Check for lowercase
    if (/[a-z]/.test(value)) strength++;
    else errors.noLowercase = true;

    // Check for numbers
    if (/\d/.test(value)) strength++;
    else errors.noNumbers = true;

    // Check for special characters
    if (/[!@#$%^&*(),.?":{}|<>]/.test(value)) strength++;
    else errors.noSpecialChars = true;

    // Return errors if password is weak
    if (strength < 3) {
      return { weakPassword: errors };
    }

    return null;
  }

  phoneNumberValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    // Remove any non-digit characters
    const cleanNumber = value.replace(/\D/g, '');

    // Check if it's exactly 11 digits
    if (cleanNumber.length !== 11) {
      return { invalidPhoneLength: { required: 11, actual: cleanNumber.length } };
    }

    // Check if it starts with valid phone number patterns (e.g., 01 for mobile)
    if (!cleanNumber.startsWith('01')) {
      return { invalidPhoneStart: true };
    }

    return null;
  }

  nationalNumberValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    // Remove any non-digit characters
    const cleanNumber = value.replace(/\D/g, '');

    // Check if it's exactly 16 digits
    if (cleanNumber.length !== 16) {
      return { invalidNationalNumber: { required: 16, actual: cleanNumber.length } };
    }

    // Check for repeated digits (like 1111111111111111)
    if (/^(\d)\1+$/.test(cleanNumber)) {
      return { repeatedDigits: true };
    }

    return null;
  }

  // Stepper methods
  nextStep(): void {
    if (this.isCurrentStepValid()) {
      if (this.currentStep < this.totalSteps) {
        this.currentStep++;
      }
    }
  }

  previousStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  isCurrentStepValid(): boolean {
    switch (this.currentStep) {
      case 1:
        return this.f['fullName'].valid && this.f['email'].valid;
      case 2:
        return this.f['password'].valid && this.f['confirmPassword'].valid &&
          this.f['grade'].valid && this.f['phoneNumber'].valid;
      case 3:
        return this.f['nationalNumber'].valid && this.f['dateOfBirth'].valid &&
          this.f['collageId'].valid;
      default:
        return false;
    }
  }

  getStepTitle(): string {
    switch (this.currentStep) {
      case 1:
        return 'Personal Information';
      case 2:
        return 'Account Security';
      case 3:
        return 'Additional Details';
      default:
        return '';
    }
  }

  mustMatch(password: string, confirmPassword: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[password];
      const matchingControl = formGroup.controls[confirmPassword];

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

  ageValidator(minAge: number) {
    return (control: AbstractControl) => {
      if (!control.value) return null;

      const birthDate = new Date(control.value);
      const today = new Date();
      const age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();

      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        return age < minAge ? { underage: true } : null;
      }

      return age < minAge ? { underage: true } : null;
    };
  }

  // Helper methods for template
  getFieldError(fieldName: string): string[] {
    const field = this.f[fieldName];
    if (!field || !field.errors) return [];

    const errors: string[] = [];

    switch (fieldName) {
      case 'fullName':
        if (field.errors['required']) errors.push('Full name is required');
        if (field.errors['minlength']) errors.push('Full name must be at least 2 characters');
        if (field.errors['maxlength']) errors.push('Full name must not exceed 50 characters');
        if (field.errors['invalidName']) errors.push('Name can only contain letters, spaces, hyphens, and apostrophes');
        break;

      case 'email':
        if (field.errors['required']) errors.push('Email is required');
        if (field.errors['email']) errors.push('Please enter a valid email format');
        if (field.errors['invalidEmail']) errors.push('Please enter a valid email address');
        if (field.errors['uncommonDomain']) errors.push('Please use a common email provider');
        break;

      case 'password':
        if (field.errors['required']) errors.push('Password is required');
        if (field.errors['minlength']) errors.push('Password must be at least 8 characters');
        if (field.errors['weakPassword']) {
          const weakErrors = field.errors['weakPassword'];
          if (weakErrors.noUppercase) errors.push('Password must contain uppercase letters');
          if (weakErrors.noLowercase) errors.push('Password must contain lowercase letters');
          if (weakErrors.noNumbers) errors.push('Password must contain numbers');
          if (weakErrors.noSpecialChars) errors.push('Password must contain special characters');
        }
        break;

      case 'confirmPassword':
        if (field.errors['required']) errors.push('Please confirm your password');
        if (field.errors['mustMatch']) errors.push('Passwords must match');
        break;

      case 'grade':
        if (field.errors['required']) errors.push('Grade is required');
        if (field.errors['min']) errors.push('Grade must be at least 1');
        if (field.errors['max']) errors.push('Grade must be at most 12');
        break;

      case 'phoneNumber':
        if (field.errors['required']) errors.push('Phone number is required');
        if (field.errors['invalidPhoneLength']) {
          const required = field.errors['invalidPhoneLength'].required;
          const actual = field.errors['invalidPhoneLength'].actual;
          errors.push(`Phone number must be exactly ${required} digits (you entered ${actual})`);
        }
        if (field.errors['invalidPhoneStart']) errors.push('Phone number must start with 01');
        break;

      case 'nationalNumber':
        if (field.errors['required']) errors.push('National number is required');
        if (field.errors['invalidNationalNumber']) {
          const required = field.errors['invalidNationalNumber'].required;
          const actual = field.errors['invalidNationalNumber'].actual;
          errors.push(`National number must be exactly ${required} digits (you entered ${actual})`);
        }
        if (field.errors['repeatedDigits']) errors.push('National number cannot be repeated digits');
        break;

      case 'dateOfBirth':
        if (field.errors['required']) errors.push('Date of birth is required');
        if (field.errors['underage']) errors.push('You must be at least 16 years old');
        break;

      case 'collageId':
        if (field.errors['required']) errors.push('College ID is required');
        if (field.errors['minlength']) errors.push('College ID must be at least 3 characters');
        if (field.errors['maxlength']) errors.push('College ID must not exceed 20 characters');
        break;
    }

    return errors;
  }

  // Input restriction methods
  onTextInput(event: any): void {
    // Allow only letters, spaces, hyphens, and apostrophes
    const value = event.target.value;
    const cleanedValue = value.replace(/[^a-zA-Z\s'-]/g, '');
    if (value !== cleanedValue) {
      event.target.value = cleanedValue;
      // Update the form control value
      const formControl = event.target.formControlName;
      if (formControl && this.registerForm.get(formControl)) {
        this.registerForm.get(formControl)?.setValue(cleanedValue, { emitEvent: false });
      }
    }
  }

  onPhoneNumberInput(event: any): void {
    // Allow numbers, +, and common phone number characters
    let value = event.target.value;
    const cleanedValue = value.replace(/[^0-9+\-\s()]/g, '');

    // Enforce maximum 11 digits (remove non-digits for counting)
    const digitsOnly = cleanedValue.replace(/\D/g, '');
    if (digitsOnly.length > 11) {
      // Simply truncate to 11 digits and remove formatting
      value = digitsOnly.substring(0, 11);
    } else {
      value = cleanedValue;
    }

    if (event.target.value !== value) {
      event.target.value = value;
      // Update the form control value
      const formControl = event.target.formControlName;
      if (formControl && this.registerForm.get(formControl)) {
        this.registerForm.get(formControl)?.setValue(value, { emitEvent: false });
      }
    }
  }

  onNumberInput(event: any): void {
    // Allow only numbers
    let value = event.target.value;
    const cleanedValue = value.replace(/[^0-9]/g, '');

    // Check if this is for national number field and enforce 16 digits limit
    const formControlName = event.target.getAttribute('formControlName');
    if (formControlName === 'nationalNumber' && cleanedValue.length > 16) {
      value = cleanedValue.substring(0, 16);
    } else {
      value = cleanedValue;
    }

    if (event.target.value !== value) {
      event.target.value = value;
      // Update the form control value
      const formControl = event.target.formControlName;
      if (formControl && this.registerForm.get(formControl)) {
        this.registerForm.get(formControl)?.setValue(value, { emitEvent: false });
      }
    }
  }

  onEmailInput(event: any): void {
    // Allow email-friendly characters
    const value = event.target.value;
    const cleanedValue = value.replace(/[^a-zA-Z0-9._%+-@]/g, '');
    if (value !== cleanedValue) {
      event.target.value = cleanedValue;
      // Update the form control value
      const formControl = event.target.formControlName;
      if (formControl && this.registerForm.get(formControl)) {
        this.registerForm.get(formControl)?.setValue(cleanedValue, { emitEvent: false });
      }
    }
  }

  onCollegeIdInput(event: any): void {
    // Allow alphanumeric characters and common ID symbols
    const value = event.target.value;
    const cleanedValue = value.replace(/[^a-zA-Z0-9\-_]/g, '');
    if (value !== cleanedValue) {
      event.target.value = cleanedValue;
      // Update the form control value
      const formControl = event.target.formControlName;
      if (formControl && this.registerForm.get(formControl)) {
        this.registerForm.get(formControl)?.setValue(cleanedValue, { emitEvent: false });
      }
    }
  }

  // Prevent paste events with invalid characters
  onPaste(event: ClipboardEvent, fieldType: string): void {
    event.preventDefault();
    const pastedData = event.clipboardData?.getData('text') || '';
    let cleanedData = '';

    switch (fieldType) {
      case 'text':
        cleanedData = pastedData.replace(/[^a-zA-Z\s'-]/g, '');
        break;
      case 'number':
        cleanedData = pastedData.replace(/[^0-9]/g, '');
        break;
      case 'phone':
        cleanedData = pastedData.replace(/[^0-9+\-\s()]/g, '');
        // Enforce 11 digits limit for phone numbers
        const phoneDigits = cleanedData.replace(/\D/g, '');
        if (phoneDigits.length > 11) {
          cleanedData = phoneDigits.substring(0, 11);
        }
        break;
      case 'email':
        cleanedData = pastedData.replace(/[^a-zA-Z0-9._%+-@]/g, '');
        break;
      case 'collegeId':
        cleanedData = pastedData.replace(/[^a-zA-Z0-9\-_]/g, '');
        break;
      default:
        cleanedData = pastedData;
    }

    // Insert the cleaned data at cursor position
    const input = event.target as HTMLInputElement;
    const start = input.selectionStart || 0;
    const end = input.selectionEnd || 0;
    const currentValue = input.value;
    const newValue = currentValue.substring(0, start) + cleanedData + currentValue.substring(end);

    // Check for national number length limit
    const formControlName = input.getAttribute('formControlName');
    let finalValue = newValue;

    if (formControlName === 'nationalNumber') {
      const digitsOnly = newValue.replace(/\D/g, '');
      if (digitsOnly.length > 16) {
        finalValue = digitsOnly.substring(0, 16);
      }
    }

    // Update the form control
    if (formControlName && this.registerForm.get(formControlName)) {
      this.registerForm.get(formControlName)?.setValue(finalValue);
      // Set cursor position after pasted content
      setTimeout(() => {
        input.setSelectionRange(start + cleanedData.length, start + cleanedData.length);
      }, 0);
    }
  }

  getPasswordStrength(): number {
    const password = this.f['password'].value;
    if (!password) return 0;

    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++;

    return strength;
  }

  getPasswordStrengthColor(): string {
    const strength = this.getPasswordStrength();
    if (strength <= 2) return '#ef4444'; // red
    if (strength <= 3) return '#f59e0b'; // yellow
    return '#10b981'; // green
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.registerForm.invalid) {
      // Focus on the first invalid field
      const firstInvalidField = Object.keys(this.f).find(key => this.f[key].invalid);
      if (firstInvalidField) {
        const element = document.getElementById(firstInvalidField);
        if (element) {
          element.focus();
          element.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
      }
      return;
    }

    this.loading = true;
    const registerRequest: RegisterRequest = {
      fullName: this.f['fullName'].value.trim(),
      email: this.f['email'].value.trim().toLowerCase(),
      password: this.f['password'].value,
      grade: Number(this.f['grade'].value),
      phoneNumber: this.f['phoneNumber'].value.replace(/\D/g, ''),
      nationalNumber: this.f['nationalNumber'].value.replace(/\D/g, ''),
      dateOfBirth: this.f['dateOfBirth'].value,
      collageId: this.f['collageId'].value.trim(),
      roles: this.f['roles'].value
    };

    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.success = 'Registration successful! Please check your email for OTP verification.';
        this.loading = false;
        // Navigate to OTP verification page after a short delay
        setTimeout(() => {
          this.router.navigate(['/auth/verify-otp'], {
            queryParams: { email: this.f['email'].value }
          });
        }, 2000);
      },
      error: (error) => {
        console.error('Registration error:', error);
        if (error.status === 409) {
          this.error = 'An account with this email already exists. Please use a different email.';
        } else if (error.status === 400) {
          this.error = error.error?.message || 'Invalid registration data. Please check your inputs.';
        } else {
          this.error = 'Registration failed. Please try again later.';
        }
        this.loading = false;
      }
    });
  }
}
