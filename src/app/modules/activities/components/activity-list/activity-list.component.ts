import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, switchMap } from 'rxjs/operators';
import { Activity, ActivityCategory } from '../../../../core/models/activity.model';
import { ActivityService } from '../../../../core/services/activity.service';
import { AuthService } from '../../../../core/services/auth.service';
import { ApiService } from '../../../../core/services/api.service';
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
  allActivities: Activity[] = []; // Store all activities for filtering
  categories: ActivityCategory[] = [];
  selectedCategory: string = 'all';
  loading = true;
  error: string | null = null;
  searchTerm: string = '';
  showCreateDialog: boolean = false;
  activitiesCount: number = 0;
  private destroy$ = new Subject<void>();

  constructor(
    private activityService: ActivityService,
    private router: Router,
    private authService: AuthService,
    private apiService: ApiService
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadActivities();
    this.loadActivitiesCount();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCategories(): void {
    // Static categories matching the activity create component
    this.categories = [
      { id: 1, name: 'technology', description: 'Technology related activities', color: '#3b82f6' },
      { id: 2, name: 'sports', description: 'Sports and fitness activities', color: '#10b981' },
      { id: 3, name: 'arts', description: 'Arts and creative activities', color: '#f59e0b' },
      { id: 4, name: 'Academic', description: 'Academic and educational activities', color: '#8b5cf6' },
      { id: 5, name: 'social', description: 'Social and community activities', color: '#ef4444' },
      { id: 6, name: 'cultural', description: 'Cultural and diversity activities', color: '#ec4899' }
    ];
  }

  loadActivities(): void {
    this.loading = true;
    this.error = null;

    // Always fetch all activities from backend
    this.activityService.getActivities(undefined).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (activities) => {
        // Fetch president names for activities that have presidentId but no presidentName
        this.fetchPresidentNames(activities).then(updatedActivities => {
          this.allActivities = updatedActivities;
          this.applyFilters();
          this.loading = false;
        });
      },
      error: (error) => {
        console.error('Error loading activities:', error);
        this.error = 'Failed to load activities';
        this.loading = false;
      }
    });
  }

  private async fetchPresidentNames(activities: Activity[]): Promise<Activity[]> {
    const activitiesWithNullPresidents = activities.filter(activity =>
      activity.presidentId && !activity.presidentName
    );

    if (activitiesWithNullPresidents.length === 0) {
      return activities;
    }

    // Fetch president names for each activity that needs it
    const fetchPromises = activitiesWithNullPresidents.map(async activity => {
      try {
        const president = await this.apiService.get<any>(`/admin/students/${activity.presidentId}`).toPromise();
        activity.presidentName = president.fullName || 'Unknown President';
      } catch (error) {
        console.error(`Error fetching president ${activity.presidentId}:`, error);
        activity.presidentName = 'Unknown President';
      }
      return activity;
    });

    await Promise.all(fetchPromises);
    return activities;
  }

  onCategoryChange(category: string): void {
    this.selectedCategory = category;
    this.applyFilters();
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  private applyFilters(): void {
    let filtered = this.allActivities;

    // Apply category filter
    if (this.selectedCategory !== 'all') {
      filtered = filtered.filter(activity =>
        activity.category === this.selectedCategory
      );
    }

    // Apply search filter
    if (this.searchTerm.trim()) {
      const searchLower = this.searchTerm.toLowerCase();
      filtered = filtered.filter(activity =>
        activity.name.toLowerCase().includes(searchLower) ||
        activity.description.toLowerCase().includes(searchLower)
      );
    }

    this.activities = filtered;
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
    this.loadActivitiesCount();
  }

  loadActivitiesCount(): void {
    this.activityService.getActivitiesCount().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (count) => {
        this.activitiesCount = count;
      },
      error: (error) => {
        console.error('Error loading activities count:', error);
        this.activitiesCount = 0;
      }
    });
  }

  isSuperAdmin(): boolean {
    const userRole = this.authService.getUserRole();
    return userRole === 'super_admin';
  }

  getPresidentInitials(fullName?: string): string {
    if (!fullName) return 'N/A';
    const names = fullName.split(' ');
    if (names.length >= 2) {
      return `${names[0][0]}${names[1][0]}`.toUpperCase();
    }
    return names[0][0].toUpperCase();
  }

  getPresidentName(activity: Activity): string {
    // First try the presidentName field
    if (activity.presidentName) {
      return activity.presidentName;
    }

    // Fallback: try to get from nested president object
    if (activity.president?.fullName) {
      return activity.president.fullName;
    }

    // If no name available, return placeholder
    return 'No President';
  }
}
