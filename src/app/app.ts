import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GlassmorphismLoginComponent } from './components/glassmorphism-login';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, GlassmorphismLoginComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('EMS-frontend');
  loginMessage = '';

  constructor() { }

  onLogin(credentials: { username: string, password: string, rememberMe: boolean }): void {
    console.log('Login attempt:', credentials);
    this.loginMessage = `Welcome back, ${credentials.username}! ${credentials.rememberMe ? '(We\'ll remember you)' : ''}`;

    // Simulate successful login
    setTimeout(() => {
      this.loginMessage = '';
    }, 5000);
  }

  onForgotPassword(): void {
    console.log('Forgot password clicked');
    this.loginMessage = 'Password reset link sent to your email!';

    setTimeout(() => {
      this.loginMessage = '';
    }, 3000);
  }

  onSignUp(): void {
    console.log('Sign up clicked');
    this.loginMessage = 'Redirecting to registration page...';

    setTimeout(() => {
      this.loginMessage = '';
    }, 2000);
  }
}
