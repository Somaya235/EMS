import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-glassmorphism-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './glassmorphism-login.component.html',
  styleUrls: ['./glassmorphism-login.component.css']
})
export class GlassmorphismLoginComponent {
  username: string = '';
  password: string = '';
  rememberMe: boolean = false;
  showPassword: boolean = false;
  isLoading: boolean = false;

  @Output() onLogin = new EventEmitter<{ username: string, password: string, rememberMe: boolean }>();
  @Output() onForgotPassword = new EventEmitter<void>();
  @Output() onSignUp = new EventEmitter<void>();

  constructor() { }

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.username && this.password) {
      this.isLoading = true;
      // Simulate loading
      setTimeout(() => {
        this.onLogin.emit({
          username: this.username,
          password: this.password,
          rememberMe: this.rememberMe
        });
        this.isLoading = false;
      }, 1500);
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onForgotPasswordClick(): void {
    this.onForgotPassword.emit();
  }

  onSignUpClick(): void {
    this.onSignUp.emit();
  }
}
