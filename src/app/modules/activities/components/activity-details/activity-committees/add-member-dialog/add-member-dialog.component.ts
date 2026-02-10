import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../../../core/services/api.service';
import { TokenStorageService } from '../../../../../../core/services/token-storage.service';

@Component({
  selector: 'app-add-member-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-member-dialog.component.html'
})
export class AddMemberDialogComponent {
  @Input() committeeId: number | null = null;
  @Input() isVisible = false;
  @Output() close = new EventEmitter<void>();
  @Output() memberAdded = new EventEmitter<void>();

  students: any[] = [];
  filteredStudents: any[] = [];
  loading = false;
  error: string | null = null;
  selectedStudentIds: number[] = [];
  searchTerm = '';

  constructor(
    private apiService: ApiService,
    private tokenStorage: TokenStorageService
  ) { }

  ngOnInit(): void {
    if (this.isVisible) {
      this.loadStudents();
    }
  }

  ngOnChanges(): void {
    if (this.isVisible && this.students.length === 0) {
      this.loadStudents();
    }
  }

  loadStudents(): void {
    this.loading = true;
    this.error = null;

    this.apiService.get<any[]>('/committees/all-students').subscribe({
      next: (students: any[]) => {
        this.students = students || [];
        this.filteredStudents = [...this.students];
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error loading students:', err);
        this.error = 'Failed to load students.';
        this.loading = false;
      }
    });
  }

  filterStudents(): void {
    if (!this.searchTerm) {
      this.filteredStudents = [...this.students];
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredStudents = this.students.filter(student =>
        student.fullName.toLowerCase().includes(term) ||
        student.email.toLowerCase().includes(term) ||
        student.collageId?.toString().includes(term)
      );
    }
  }

  onSearchChange(): void {
    this.filterStudents();
  }

  selectStudent(studentId: number): void {
    const index = this.selectedStudentIds.indexOf(studentId);
    if (index > -1) {
      // Remove from selection if already selected
      this.selectedStudentIds.splice(index, 1);
    } else {
      // Add to selection if not selected
      this.selectedStudentIds.push(studentId);
    }
  }

  isStudentSelected(studentId: number): boolean {
    return this.selectedStudentIds.includes(studentId);
  }

  addMembers(): void {
    if (!this.selectedStudentIds.length || !this.committeeId) {
      return;
    }

    this.loading = true;
    this.error = null;

    // Add all selected students one by one
    const addPromises = this.selectedStudentIds.map(studentId => {
      const requestDTO = {
        studentId: studentId
      };
      return this.apiService.post(`/committees/${this.committeeId}/members`, requestDTO).toPromise();
    });

    Promise.all(addPromises).then(() => {
      this.loading = false;
      this.memberAdded.emit();
      this.closeDialog();
    }).catch((err) => {
      console.error('Error adding members:', err);
      this.error = 'Failed to add one or more members. Please try again.';
      this.loading = false;
    });
  }

  closeDialog(): void {
    this.isVisible = false;
    this.selectedStudentIds = [];
    this.searchTerm = '';
    this.filteredStudents = [...this.students];
    this.close.emit();
  }

  getInitials(name: string): string {
    if (!name) return '';
    const parts = name.split(' ');
    if (parts.length > 1) {
      return parts[0].charAt(0).toUpperCase() + parts[1].charAt(0).toUpperCase();
    } else if (parts.length === 1) {
      return parts[0].charAt(0).toUpperCase();
    }
    return '';
  }

  getAvatarColor(name: string): string {
    const colors = [
      '#FF5733', '#33FF57', '#3357FF', '#FF33F6', '#F6FF33', '#33FFF6',
      '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD'
    ];
    let hash = 0;
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }
    const index = Math.abs(hash % colors.length);
    return colors[index];
  }
}
