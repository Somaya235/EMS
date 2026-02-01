import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CommitteeResponseDTO } from '../../../../../core/models';
import { CommitteeService } from '../../../../../core/services/committee.service';

@Component({
  selector: 'app-activity-committees',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './activity-committees.component.html',
  styleUrls: ['./activity-committees.component.scss']
})
export class ActivityCommitteesComponent implements OnInit, OnDestroy {
  @Input() activityId!: number;
  committees: CommitteeResponseDTO[] = [];
  committeesWithMemberCount: Array<CommitteeResponseDTO & { memberCount: number }> = [];
  loading = true;
  error: string | null = null;
  private destroy$ = new Subject<void>();

  constructor(private committeeService: CommitteeService) { }

  ngOnInit(): void {
    if (this.activityId) {
      this.loadCommittees();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCommittees(): void {
    this.loading = true;
    this.error = null;

    this.committeeService.getCommitteesByActivity(this.activityId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (committees) => {
        console.log('=== COMPONENT DEBUGGING ===');
        console.log('Committees received in component:', committees);
        console.log('Type:', typeof committees);
        console.log('Is array?', Array.isArray(committees));
        console.log('Length:', committees?.length);

        if (committees && committees.length > 0) {
          console.log('First committee raw:', committees[0]);
          console.log('First committee keys:', Object.keys(committees[0]));
          console.log('First committee name value:', committees[0].name);
          console.log('First committee name type:', typeof committees[0].name);
          console.log('First committee head value:', committees[0].head);
          console.log('First committee head type:', typeof committees[0].head);

          // Try to access nested properties
          if (committees[0].head) {
            console.log('Head fullName:', committees[0].head.fullName);
          } else {
            console.log('Head is null/undefined');
          }
        }

        console.log('=== END COMPONENT DEBUGGING ===');

        this.committees = committees;
        this.loadMemberCounts();
      },
      error: (err) => {
        console.error('Error loading committees:', err);
        this.error = 'Failed to load committees.';
        this.loading = false;
      }
    });
  }

  private loadMemberCounts(): void {
    const memberCountRequests = this.committees.map(committee =>
      this.committeeService.countCommitteeMembers(committee.id).pipe(
        takeUntil(this.destroy$)
      )
    );

    // Load all member counts
    Promise.all(memberCountRequests.map(req => req.toPromise())).then(
      memberCounts => {
        this.committeesWithMemberCount = this.committees.map((committee, index) => ({
          ...committee,
          memberCount: memberCounts[index] || 0
        }));
        this.loading = false;
      },
      error => {
        console.error('Error loading member counts:', error);
        // Still show committees even if member counts fail
        this.committeesWithMemberCount = this.committees.map(committee => ({
          ...committee,
          memberCount: 0
        }));
        this.loading = false;
      }
    );
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

  getCommitteeColor(index: number): string {
    const colors = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#ef4444', '#ec4899'];
    return colors[index % colors.length];
  }
}
