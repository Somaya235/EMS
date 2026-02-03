import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../../../core/services/api.service';

@Component({
  selector: 'app-edit-committee-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-committee-dialog.component.html',
  styleUrls: ['./edit-committee-dialog.component.scss']
})
export class EditCommitteeDialogComponent {
  @Input() committeeId: number | null = null;
  @Input() isVisible = false;
  @Input() committeeData: any = null;
  @Output() close = new EventEmitter<void>();
  @Output() committeeUpdated = new EventEmitter<void>();

  formData = {
    name: '',
    description: '',
    headId: null as number | null,
    directorId: null as number | null
  };

  availableUsers: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private apiService: ApiService
  ) { }

  ngOnInit(): void {
    if (this.isVisible && this.committeeData) {
      this.initializeFormData();
      this.loadAvailableUsers();
    }
  }

  ngOnChanges(): void {
    if (this.isVisible && this.committeeData) {
      this.initializeFormData();
      this.loadAvailableUsers();
    }
  }

  private initializeFormData(): void {
    if (this.committeeData) {
      this.formData = {
        name: this.committeeData.name || '',
        description: this.committeeData.description || '',
        headId: this.committeeData.head?.id || null,
        directorId: this.committeeData.director?.id || null
      };
    }
  }

  private loadAvailableUsers(): void {
    this.loading = true;
    this.apiService.get('/committees/all-students').subscribe({
      next: (users: any) => {
        this.availableUsers = users;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error loading users:', err);
        this.error = 'Failed to load available users.';
        this.loading = false;
      }
    });
  }

  updateCommittee(): void {
    if (!this.committeeId || !this.formData.name.trim()) {
      return;
    }

    this.loading = true;
    this.error = null;

    const updateData = {
      name: this.formData.name.trim(),
      description: this.formData.description.trim(),
      activityId: this.committeeData?.activity?.id, // Include required activityId
      headId: this.formData.headId || this.committeeData?.head?.id, // Ensure headId is provided
      directorId: this.formData.directorId
    };

    this.apiService.put(`/committees/${this.committeeId}`, updateData).subscribe({
      next: () => {
        this.loading = false;
        this.committeeUpdated.emit();
        this.closeDialog();
      },
      error: (err: any) => {
        console.error('Error updating committee:', err);
        this.error = 'Failed to update committee. Please try again.';
        this.loading = false;
      }
    });
  }

  closeDialog(): void {
    this.isVisible = false;
    this.error = null;
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
