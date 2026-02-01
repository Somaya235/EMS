import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Activity, ActivityCategory } from '../../../../core/models/activity.model';
import { ActivityService } from '../../../../core/services/activity.service';
import { AuthService } from '../../../../core/services/auth.service';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { ActivityCreateComponent } from '../activity-create/activity-create.component';

@Component({
  selector: 'app-activity-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent, ActivityCreateComponent],
  templateUrl: './activity-list.component.html',
  styleUrls: ['./activity-list.component.scss']
})
export class ActivityListComponent implements OnInit, OnDestroy {
  activities: Activity[] = [];
  categories: ActivityCategory[] = [];
  selectedCategory: string = 'all';
  loading = true;
  error: string | null = null;
  searchTerm: string = '';
  showCreateDialog: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private activityService: ActivityService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadActivities();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCategories(): void {
    // Static categories matching the activity create component
    this.categories = [
      { id: 1, name: 'Technology', description: 'Technology related activities', color: '#3b82f6' },
      { id: 2, name: 'Sports', description: 'Sports and fitness activities', color: '#10b981' },
      { id: 3, name: 'Arts', description: 'Arts and creative activities', color: '#f59e0b' },
      { id: 4, name: 'Academic', description: 'Academic and educational activities', color: '#8b5cf6' },
      { id: 5, name: 'Social', description: 'Social and community activities', color: '#ef4444' },
      { id: 6, name: 'Cultural', description: 'Cultural and diversity activities', color: '#ec4899' }
    ];
  }

  loadActivities(): void {
    this.loading = true;
    this.error = null;

    const category = this.selectedCategory === 'all' ? undefined : this.selectedCategory;

    this.activityService.getActivities(category).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (activities) => {
        this.activities = this.filterActivities(activities);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading activities:', error);
        this.error = 'Failed to load activities';
        this.loading = false;
      }
    });
  }

  onCategoryChange(category: string): void {
    this.selectedCategory = category;
    this.loadActivities();
  }

  onSearchChange(): void {
    // Filter activities based on search term
    this.activities = this.filterActivities(this.activities);
  }

  private filterActivities(activities: Activity[]): Activity[] {
    let filtered = activities;

    // Filter by search term
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filtered = filtered.filter(activity =>
        activity.name.toLowerCase().includes(searchLower) ||
        activity.description.toLowerCase().includes(searchLower)
      );
    }

    return filtered;
  }

  viewActivityDetails(activityId: number): void {
    this.router.navigate(['/activities', activityId]);
  }

  joinActivity(activityId: number): void {
    this.activityService.joinActivity(activityId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => {
        // Refresh activities to update member count
        this.loadActivities();
      },
      error: (error) => {
        console.error('Error joining activity:', error);
      }
    });
  }

  getCategoryColor(category: string): string {
    const categoryObj = this.categories.find(cat => cat.name === category);
    return categoryObj?.color || '#6366f1';
  }

  openCreateDialog(): void {
    this.showCreateDialog = true;
  }

  closeCreateDialog(): void {
    this.showCreateDialog = false;
  }

  onActivityCreated(): void {
    this.loadActivities();
  }

  isSuperAdmin(): boolean {
    const userRole = this.authService.getUserRole();
    return userRole === 'super_admin';
  }
}
