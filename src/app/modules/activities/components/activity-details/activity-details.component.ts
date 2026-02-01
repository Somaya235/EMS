import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Activity } from '../../../../core/models/activity.model';
import { ActivityService } from '../../../../core/services/activity.service';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { ActivityCommitteesComponent } from "./activity-committees/activity-committees.component";

@Component({
  selector: 'app-activity-details',
  standalone: true,
  imports: [CommonModule, HeaderComponent, ActivityCommitteesComponent],
  templateUrl: './activity-details.component.html',
  styleUrls: ['./activity-details.component.scss']
})
export class ActivityDetailsComponent implements OnInit {
  activity$!: Observable<Activity | null>;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activityService: ActivityService
  ) { }

  ngOnInit(): void {
    this.activity$ = this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id) {
          return this.activityService.getActivityById(+id).pipe(
            catchError(error => {
              this.error = 'Failed to load activity details';
              this.loading = false;
              return of(null);
            })
          );
        }
        this.error = 'Activity ID not found';
        this.loading = false;
        return of(null);
      })
    );

    this.activity$.subscribe(activity => {
      if (activity) {
        this.loading = false;
      }
    });
  }

  joinActivity(activityId: number): void {
    this.activityService.joinActivity(activityId).subscribe({
      next: () => {
        // Refresh the activity details
        this.ngOnInit();
      },
      error: (error) => {
        console.error('Error joining activity:', error);
      }
    });
  }

  leaveActivity(activityId: number): void {
    this.activityService.leaveActivity(activityId).subscribe({
      next: () => {
        // Refresh the activity details
        this.ngOnInit();
      },
      error: (error) => {
        console.error('Error leaving activity:', error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/activities']);
  }

  getPresidentInitials(fullName?: string): string {
    if (!fullName) return 'N/A';
    return fullName
      .split(' ')
      .map(word => word.charAt(0))
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }

  getCategoryColor(category: string): string {
    const categoryColors: { [key: string]: string } = {
      'technology': '#3b82f6',
      'sports': '#10b981',
      'arts': '#f59e0b',
      'academic': '#8b5cf6',
      'social': '#ef4444',
      'cultural': '#ec4899'
    };
    return categoryColors[category.toLowerCase()] || '#8b5cf6';
  }

  getActivityRequirements(): string[] {
    // This method will be called from the template with the activity context
    return [];
  }

  getActivityRequirementsForActivity(activity: Activity): string[] {
    // Use real requirements from activity if available, otherwise use defaults
    if (activity.requirements && activity.requirements.length > 0) {
      return activity.requirements;
    }

    return this.getDefaultRequirements(activity.category);
  }

  // Helper method to get requirements when activity is loaded
  private getDefaultRequirements(category?: string): string[] {
    const categoryRequirements: { [key: string]: string[] } = {
      'technology': [
        'Basic programming knowledge',
        'Laptop with required software',
        'Attendance at weekly meetings',
        'Willingness to learn new technologies'
      ],
      'sports': [
        'Physical fitness clearance',
        'Proper sports equipment',
        'Commitment to regular practice',
        'Teamwork and sportsmanship'
      ],
      'arts': [
        'Creative portfolio or samples',
        'Basic art supplies',
        'Participation in exhibitions',
        'Openness to feedback'
      ],
      'academic': [
        'Minimum GPA requirement',
        'Academic references',
        'Research interest',
        'Regular attendance'
      ],
      'social': [
        'Communication skills',
        'Event planning experience',
        'Community service hours',
        'Leadership potential'
      ],
      'cultural': [
        'Cultural awareness',
        'Language proficiency',
        'Performance skills',
        'Respect for diversity'
      ]
    };

    const categoryKey = category?.toLowerCase();
    if (categoryKey && categoryKey in categoryRequirements) {
      return categoryRequirements[categoryKey];
    }

    return [
      'Basic knowledge of ' + (category || 'the activity'),
      'Commitment to attend weekly meetings',
      'Willingness to participate in events',
      'Positive attitude and teamwork'
    ];
  }
}
