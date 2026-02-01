import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../../../core/models';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  loading = false;
  searchTerm = '';

  constructor() { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    // Mock data - replace with actual API call
    this.users = [
      {
        id: 1,
        fullName: 'Admin User',
        email: 'admin@example.com',
        grade: 'Senior',
        roles: ['ADMIN'],
        createdAt: '2023-01-01',
        updatedAt: '2023-01-01'
      },
      {
        id: 2,
        fullName: 'Regular User',
        email: 'user1@example.com',
        grade: 'Junior',
        roles: ['USER'],
        createdAt: '2023-01-15',
        updatedAt: '2023-01-15'
      }
    ];
    this.loading = false;
  }

  searchUsers(): void {
    // Implement search logic
    console.log('Searching for:', this.searchTerm);
  }

  editUser(user: User): void {
    // Implement edit user logic
    console.log('Editing user:', user);
  }

  deleteUser(user: User): void {
    // Implement delete user logic
    console.log('Deleting user:', user);
  }

  toggleUserStatus(user: User): void {
    // Implement toggle user status logic
    console.log('Toggling status for user:', user);
  }
}
