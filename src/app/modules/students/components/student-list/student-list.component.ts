import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { User } from '../../../../core/models/user.model';
import { UserService } from '../../services/user.service';
import { HeaderComponent } from '../../../../shared/components/header/header.component';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  providers: [UserService],
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.scss']
})
export class StudentListComponent implements OnInit, OnDestroy {
  students: User[] = [];
  allStudents: User[] = [];
  loading = true;
  error: string | null = null;
  searchTerm: string = '';
  private destroy$ = new Subject<void>();

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadStudents();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadStudents(): void {
    this.loading = true;
    this.error = null;

    this.userService.getStudents().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (students) => {
        console.log('Received students data:', students);
        this.allStudents = students;
        this.students = this.filterStudents(students);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading students:', error);
        this.error = 'Failed to load students';
        this.loading = false;
      }
    });
  }

  onSearchChange(): void {
    this.students = this.filterStudents(this.allStudents);
  }

  private filterStudents(students: User[]): User[] {
    if (!this.searchTerm.trim()) {
      return students;
    }

    const searchLower = this.searchTerm.toLowerCase();
    return students.filter(student =>
      (student.fullName?.toLowerCase() || '').includes(searchLower) ||
      (student.email?.toLowerCase() || '').includes(searchLower) ||
      (student.grade?.toLowerCase() || '').includes(searchLower)
    );
  }

  viewStudentProfile(student: User): void {
    this.router.navigate(['/students', student.id]);
  }
}
