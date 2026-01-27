import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    const userRole = this.authService.getUserRole();
    // Add your role-based logic here
    if (userRole) {
      return true;
    } else {
      this.router.navigate(['/auth/login']);
      return false;
    }
  }
}
