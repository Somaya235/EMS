import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-system-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './system-settings.component.html',
  styleUrls: ['./system-settings.component.scss']
})
export class SystemSettingsComponent implements OnInit {
  settings = {
    siteName: 'EMS',
    siteDescription: 'Event Management System',
    allowRegistration: true,
    emailVerification: true,
    maintenanceMode: false,
    maxFileSize: 5,
    supportedFileTypes: ['pdf', 'doc', 'docx', 'jpg', 'png'],
    sessionTimeout: 30
  };

  constructor() { }

  ngOnInit(): void { }

  saveSettings(): void {
    console.log('Saving settings:', this.settings);
  }

  resetSettings(): void {
    console.log('Resetting settings to defaults');
  }

  testEmailSettings(): void {
    console.log('Testing email configuration');
  }
}
