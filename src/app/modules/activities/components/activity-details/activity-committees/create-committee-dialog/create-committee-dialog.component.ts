import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommitteeService } from '../services/committee.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-create-committee-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-committee-dialog.component.html'
})
export class CreateCommitteeDialogComponent implements OnInit {
  @Input() isVisible: boolean = false;
  @Input() activityId: number | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();

  formData = {
    name: '',
    description: '',
    headId: null as number | null,
    directorId: null as number | null
  };

  students: any[] = [];
  loadingStudents = false;

  constructor(private committeeService: CommitteeService) { }

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.loadingStudents = true;
    this.committeeService.getAllStudents().subscribe({
      next: (students) => {
        this.students = students;
        this.loadingStudents = false;
      },
      error: (err) => {
        console.error('Error loading students:', err);
        this.loadingStudents = false;
      }
    });
  }

  onSave(): void {
    if (this.formData.name.trim() && this.formData.headId) {
      const committeeData = {
        name: this.formData.name.trim(),
        description: this.formData.description.trim(),
        activityId: this.activityId,
        headId: this.formData.headId,
        directorId: this.formData.directorId
      };
      this.save.emit(committeeData);
    }
  }

  onClose(): void {
    this.close.emit();
    this.resetForm();
  }

  private resetForm(): void {
    this.formData = {
      name: '',
      description: '',
      headId: null,
      directorId: null
    };
  }
}
