import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-edit-profile-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile-dialog.component.html',
  styleUrls: ['./edit-profile-dialog.component.scss']
})
export class EditProfileDialogComponent {
  @Input() isVisible = false;
  @Input() userData: any = null;
  @Output() close = new EventEmitter<void>();
  @Output() profileUpdated = new EventEmitter<void>();

  formData = {
    fullName: '',
    phoneNumber: '',
    nationalNumber: '',
    grade: '',
    major: '',
    dateOfBirth: '',
    collageId: ''
  };

  loading = false;
  error: string | null = null;
  isFormValid = false;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    if (this.isVisible && this.userData) {
      this.initializeFormData();
    }
  }

  ngOnChanges(): void {
    if (this.isVisible && this.userData) {
      this.initializeFormData();
    }
  }

  private initializeFormData(): void {
    if (this.userData) {
      this.formData = {
        fullName: this.userData.fullName || '',
        phoneNumber: this.userData.phoneNumber || '',
        nationalNumber: this.userData.nationalNumber || '',
        grade: this.userData.grade || '',
        major: this.userData.major || '',
        dateOfBirth: this.formatDateForInput(this.userData.dateOfBirth),
        collageId: this.userData.collageId || ''
      };
      this.validateForm();
    }
  }

  private formatDateForInput(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0]; // Format as YYYY-MM-DD
  }

  getCurrentDate(): string {
    return new Date().toISOString().split('T')[0];
  }

  getMinDate(): string {
    const today = new Date();
    const minDate = new Date(today.getFullYear() - 17, today.getMonth(), today.getDate());
    return minDate.toISOString().split('T')[0];
  }

  validateForm(): void {
    const fullNameValid = this.formData.fullName.trim().length > 0 && /^[A-Za-z\s]+$/.test(this.formData.fullName);
    const phoneValid = !this.formData.phoneNumber || /^[0-9]{11}$/.test(this.formData.phoneNumber);
    const nationalNumberValid = !this.formData.nationalNumber || /^[0-9]{16}$/.test(this.formData.nationalNumber);
    const gradeValid = !this.formData.grade || /^[0-9]+$/.test(this.formData.grade);
    const majorValid = !this.formData.major || /^[A-Za-z\s]+$/.test(this.formData.major);
    const dateValid = !this.formData.dateOfBirth || this.isAgeValid(this.formData.dateOfBirth);
    const collegeIdValid = !this.formData.collageId || /^[A-Za-z0-9]+$/.test(this.formData.collageId);

    this.isFormValid = fullNameValid && phoneValid && nationalNumberValid && gradeValid && majorValid && dateValid && collegeIdValid;
  }

  isAgeValid(dateString: string): boolean {
    if (!dateString) return true;
    const birthDate = new Date(dateString);
    const today = new Date();
    const age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      return age - 1 >= 17;
    }
    return age >= 17;
  }

  onInputChange(): void {
    this.validateForm();
  }

  updateProfile(): void {
    if (!this.userData?.id || !this.formData.fullName.trim()) {
      return;
    }

    this.loading = true;
    this.error = null;

    const updateData = {
      fullName: this.formData.fullName.trim(),
      phoneNumber: this.formData.phoneNumber.trim() || null,
      nationalNumber: this.formData.nationalNumber.trim() || null,
      grade: this.formData.grade.trim() || null,
      major: this.formData.major.trim() || null,
      dateOfBirth: this.formData.dateOfBirth || null,
      collageId: this.formData.collageId.trim() || null
    };

    this.userService.updateUserProfile(this.userData.id, updateData).subscribe({
      next: () => {
        this.loading = false;
        this.profileUpdated.emit();
        this.closeDialog();
      },
      error: (err: any) => {
        console.error('Error updating profile:', err);
        this.error = 'Failed to update profile. Please try again.';
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
