import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap, of } from 'rxjs';
import { User } from '../../../../core/models/user.model';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../../../core/services/auth.service';
import { HeaderComponent } from '../../../../shared/components/header/header.component';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule, HeaderComponent],
  templateUrl: './student-profile.html',
  styleUrl: './student-profile.css',
  providers: [UserService]
})
export class StudentProfile implements OnInit {
  student$!: Observable<User>;
  loading = true;
  error: string | null = null;
  isCurrentUserProfile = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const routeUrl = this.router.url;

    if (routeUrl.includes('/students/profile')) {
      // This is the current user's profile - use auth service data
      this.isCurrentUserProfile = true;
      const currentUser = this.authService.getCurrentUser();

      if (currentUser) {
        this.student$ = of(currentUser);
        this.loading = false;
      } else {
        this.error = 'User not logged in';
        this.loading = false;
      }
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
}
