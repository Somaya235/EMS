import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivityService } from '../../../../core/services/activity.service';
import { UserService } from '../../../students/services/user.service';
import { User } from '../../../../core/models/user.model';

export interface ActivityCreateRequest {
  name: string;
  description: string;
  category: string;
  presidentId: number;
}

@Component({
  selector: 'app-activity-create',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './activity-create.component.html',
})
export class ActivityCreateComponent implements OnInit {
  @Output() activityCreated = new EventEmitter<void>();
  @Output() dialogClosed = new EventEmitter<void>();

  activityForm: FormGroup;
  loading = false;
  error: string | null = null;
  categories = [
    { name: 'Technology', value: 'technology' },
    { name: 'Sports', value: 'sports' },
    { name: 'Arts', value: 'arts' },
    { name: 'Academic', value: 'academic' },
    { name: 'Social', value: 'social' },
    { name: 'Cultural', value: 'cultural' }
  ];
  students: User[] = [];
  studentsLoading = false;

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private userService: UserService
  ) {
    this.activityForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      category: ['technology', Validators.required],
      presidentId: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.studentsLoading = true;
    this.userService.getStudents().subscribe({
      next: (students) => {
        this.students = students;
        this.studentsLoading = false;
      },
      error: (error) => {
        console.error('Error loading students:', error);
        this.error = 'Failed to load students. Please try again.';
        this.studentsLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.activityForm.invalid) {
      this.markFormGroupTouched(this.activityForm);
      return;
    }

    this.loading = true;
    this.error = null;

    const activityData: ActivityCreateRequest = {
      name: this.activityForm.value.name,
      description: this.activityForm.value.description,
      category: this.activityForm.value.category,
      presidentId: this.activityForm.value.presidentId
    };

    this.activityService.createActivity(activityData).subscribe({
      next: () => {
        this.loading = false;
        this.activityCreated.emit();
        this.closeDialog();
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Failed to create activity. Please try again.';
      }
    });
  }

  closeDialog(): void {
    this.dialogClosed.emit();
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  get name() { return this.activityForm.get('name'); }
  get description() { return this.activityForm.get('description'); }
  get category() { return this.activityForm.get('category'); }
  get presidentId() { return this.activityForm.get('presidentId'); }
}
