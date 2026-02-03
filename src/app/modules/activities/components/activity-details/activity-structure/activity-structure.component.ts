import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApiService } from '../../../../../core/services/api.service';

interface ActivityStructure {
  president: {
    fullName: string;
  } | null;
  directors: Array<{
    activityId: number;
    directorId: number;
    directorFullName: string;
    directorEmail: string;
    positionName: string;
    jobDescription: string;
    assignedAt: string;
  }>;
  committeeHeads: Array<{
    fullName: string;
    committeeName: string;
    memberCount: number;
  }>;
  totalCommittees: number;
}

@Component({
  selector: 'app-activity-structure',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './activity-structure.component.html',
  styleUrls: ['./activity-structure.component.scss']
})
export class ActivityStructureComponent implements OnInit, OnDestroy {
  @Input() activityId!: number;

  structure: ActivityStructure = {
    president: null,
    directors: [],
    committeeHeads: [],
    totalCommittees: 0
  };

  loading = true;
  error: string | null = null;
  private destroy$ = new Subject<void>();

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    if (this.activityId) {
      this.loadActivityStructure();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadActivityStructure(): void {
    this.loading = true;
    this.error = null;

    // Load activity details to get president
    this.apiService.get(`/student-activities/${this.activityId}`).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (activity: any) => {
        // Fix president data mapping - API returns presidentName as string, not president object
        if (activity.presidentName) {
          this.structure.president = {
            fullName: activity.presidentName
          };
        } else {
          this.structure.president = null;
        }

        console.log('President data:', this.structure.president);
        console.log('Activity data:', activity);

        // Load directors
        this.loadDirectors();
      },
      error: (err) => {
        console.error('Error loading activity details:', err);
        this.error = 'Failed to load activity structure.';
        this.loading = false;
      }
    });
  }

  private loadDirectors(): void {
    // Clear directors array first to avoid duplicates
    this.structure.directors = [];

    this.apiService.get<any[]>(`/student-activities/${this.activityId}/directors`).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (directors: any[]) => {
        // Don't add activity directors for now - we'll only show committee directors
        // this.structure.directors = directors || [];

        // Load committees to get committee directors
        this.loadCommittees();
      },
      error: (err) => {
        console.error('Error loading directors:', err);
        this.loadCommittees(); // Continue with committees loading
      }
    });
  }

  private loadCommittees(): void {
    this.apiService.get<any[]>(`/committees/activity/${this.activityId}`).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: async (committees: any[]) => {
        this.structure.totalCommittees = committees?.length || 0;

        if (committees && committees.length > 0) {
          // Load member counts for each committee and extract directors
          const committeeDataPromises = committees.map(async (committee) => {
            let memberCount = 0;

            try {
              const countResponse = await this.apiService.get<number>(`/committees/${committee.id}/members/count`).toPromise();
              memberCount = countResponse || 0;
            } catch (error) {
              console.error(`Error loading member count for committee ${committee.id}:`, error);
            }

            // Extract committee head
            const headData = {
              fullName: committee.head?.fullName || 'No Head Assigned',
              committeeName: committee.name || 'Unknown Committee',
              memberCount: memberCount
            };

            // Extract committee director if exists and add to directors array
            if (committee.director) {
              this.structure.directors.push({
                activityId: this.activityId,
                directorId: committee.director.id || committee.directorId,
                directorFullName: committee.director.fullName,
                directorEmail: committee.director.email || '',
                positionName: `Director of ${committee.name || 'Unknown Committee'}`,
                jobDescription: `Committee director for ${committee.name || 'Unknown Committee'}`,
                assignedAt: new Date().toISOString()
              });
            }

            return headData;
          });

          this.structure.committeeHeads = await Promise.all(committeeDataPromises);
        }

        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading committees:', err);
        this.error = 'Failed to load committees.';
        this.loading = false;
      }
    });
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
