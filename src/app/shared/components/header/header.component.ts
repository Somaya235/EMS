import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [CommonModule, FormsModule, RouterLink]
})
export class HeaderComponent implements OnInit {
  currentUser: User | null = null;
  isDropdownOpen = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  navigateToProfile(): void {
    // Navigate to the current user's profile route
    this.router.navigate(['/students/profile']);
    this.closeDropdown();
  }

  navigateToSettings(): void {
    this.router.navigate(['/admin/settings']);
    this.closeDropdown();
  }

  getInitials(): string {
    if (!this.currentUser || !this.currentUser.fullName) return 'U';
    const names = this.currentUser.fullName.split(' ');
    if (names.length >= 2) {
      return `${names[0][0]}${names[1][0]}`.toUpperCase();
    }
    return names[0][0].toUpperCase();
  }

  getFullName(): string {
    if (!this.currentUser) return 'User';
    return this.currentUser.fullName || 'User';
  }

  isAdmin(): boolean {
    if (!this.currentUser || !this.currentUser.roles) return false;
    return this.currentUser.roles.some((role: any) => role.name === 'ADMIN');
  }

  isSuperAdmin(): boolean {
    const userRole = this.authService.getUserRole();
    return userRole === 'super_admin';
  }

  getUserRoleDisplay(): string {
    const userRole = this.authService.getUserRole();
    if (!userRole) return 'User';

    // Convert role codes to user-friendly names
    switch (userRole) {
      case 'super_admin':
        return 'Super Admin';
      case 'admin':
        return 'Admin';
      case 'member':
        return 'Student';
      default:
        return 'User';
    }
  }
}
