import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-error-message',
  templateUrl: './error-message.component.html',
  styleUrls: ['./error-message.component.scss'],
  imports: [ FormsModule, CommonModule]
})
export class ErrorMessageComponent {
  @Input() message: string = 'An error occurred';
  @Input() type: 'error' | 'warning' | 'info' = 'error';
  @Input() dismissible: boolean = false;
  @Input() showIcon: boolean = true;

  isVisible: boolean = true;

  dismiss(): void {
    this.isVisible = false;
  }

  getIconClass(): string {
    switch (this.type) {
      case 'error': return 'fas fa-exclamation-circle';
      case 'warning': return 'fas fa-exclamation-triangle';
      case 'info': return 'fas fa-info-circle';
      default: return 'fas fa-exclamation-circle';
    }
  }

  getAlertClass(): string {
    return `alert-${this.type}`;
  }
}
