import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Activity } from '../../../../core/models/activity.model';

@Component({
  selector: 'app-edit-activity-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-activity-dialog.component.html'
})
export class EditActivityDialogComponent {
  @Input() activity: Activity | null = null;
  @Input() isVisible: boolean = false;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();

  formData = {
    name: '',
    description: '',
    category: '',
    vision: '',
    mission: ''
  };

  categories = [
    'Academic',
    'Sports',
    'Cultural',
    'Technical',
    'Social',
    'Community Service',
    'Arts',
    'Music',
    'Other'
  ];

  ngOnChanges(): void {
    if (this.activity) {
      this.formData = {
        name: this.activity.name || '',
        description: this.activity.description || '',
        category: this.activity.category || '',
        vision: this.activity.vision || '',
        mission: this.activity.mission || ''
      };
    }
  }

  onClose(): void {
    this.close.emit();
  }

  onSave(): void {
    if (this.formData.name.trim() && this.formData.description.trim()) {
      this.save.emit(this.formData);
    }
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }
}
