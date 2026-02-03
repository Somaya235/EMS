import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap, of } from 'rxjs';
import { User } from '../../../../core/models/user.model';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../../../core/services/auth.service';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { EditProfileDialogComponent } from './edit-profile-dialog/edit-profile-dialog.component';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule, HeaderComponent, EditProfileDialogComponent],
  templateUrl: './student-profile.html',
  styleUrl: './student-profile.css',
  providers: [UserService]
})
export class StudentProfile implements OnInit {
  student$!: Observable<User>;
  loading = true;
  error: string | null = null;
  isCurrentUserProfile = false;
  showEditDialog = false;
  currentUserData: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const routeUrl = this.router.url;
    console.log('Profile component initialized, route:', routeUrl);

    if (routeUrl.includes('/profile')) {
      // This is the current user's profile - use auth service data
      this.isCurrentUserProfile = true;
      console.log('Loading current user profile...');

      // Always fetch fresh data instead of using cached data
      this.authService.refreshCurrentUser().subscribe({
        next: (user: User | null) => {
          console.log('Fresh user data loaded:', user);
          if (user) {
            this.student$ = of(user);
          } else {
            this.error = 'User not found';
          }
          this.loading = false;
        },
        error: (err: any) => {
          console.error('Error loading current user:', err);
          this.error = 'Failed to load user profile';
          this.loading = false;
        }
      });
    } else {
      // This is another user's profile - fetch by ID
      this.isCurrentUserProfile = false;
      this.student$ = this.route.paramMap.pipe(
        switchMap(params => {
          const id = Number(params.get('id'));
          if (isNaN(id)) {
            this.error = 'Invalid student ID';
            this.loading = false;
            return new Observable<User>();
          }
          return this.userService.getUserById(id);
        })
      );

      this.student$.subscribe({
        next: (student) => {
          console.log('Received student data:', student);
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load student profile';
          this.loading = false;
          console.error('Error loading student:', err);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/students']);
  }

  openEditDialog(student: any): void {
    this.currentUserData = student;
    this.showEditDialog = true;
  }

  closeEditDialog(): void {
    this.showEditDialog = false;
    this.currentUserData = null;
  }

  onProfileUpdated(): void {
    this.closeEditDialog();
    // Refresh the current user data from the backend
    if (this.isCurrentUserProfile) {
      this.authService.refreshCurrentUser().subscribe({
        next: (user: User | null) => {
          if (user) {
            this.student$ = of(user);
          }
        },
        error: (err: any) => {
          console.error('Error refreshing user data:', err);
        }
      });
    } else {
      // Refresh the student data by ID
      this.student$ = this.userService.getUserById(this.currentUserData?.id);
    }
  }
}
