import { Component, OnInit, HostListener } from '@angular/core';
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

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    const clickedInside = target.closest('.user-section');

    if (this.isDropdownOpen && !clickedInside) {
      this.closeDropdown();
    }
  }

  logout(): void {
    this.authService.logout().subscribe({
      error: (error) => {
        console.error('Logout error:', error);
        // Navigation is handled in the service even on error
      }
    });
  }

  navigateToProfile(): void {
    console.log('Navigating to profile...');
    this.closeDropdown();
    this.router.navigate(['/students/profile']).then(
      (success) => {
        console.log('Navigation successful:', success);
      },
      (error) => {
        console.error('Navigation failed:', error);
      }
    );
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
    const userRole = this.authService.getUserRole();
    return userRole === 'admin';
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
      case 'activity_president':
        return 'Activity President';
      case 'member':
        return 'Student';
      default:
        return 'User';
    }
  }
}
