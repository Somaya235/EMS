import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CommitteeResponseDTO } from '../../../../../core/models';
import { CommitteeService } from './services/committee.service';
import { CreateCommitteeDialogComponent } from './create-committee-dialog/create-committee-dialog.component';
import { AddMemberDialogComponent } from './add-member-dialog/add-member-dialog.component';
import { EditCommitteeDialogComponent } from './edit-committee-dialog/edit-committee-dialog.component';
import { ActivityService } from '../../../../../core/services/activity.service';
import { TokenStorageService } from '../../../../../core/services/token-storage.service';
import { Activity } from '../../../../../core/models/activity.model';

@Component({
  selector: 'app-activity-committees',
  standalone: true,
  imports: [CommonModule, CreateCommitteeDialogComponent, AddMemberDialogComponent, EditCommitteeDialogComponent],
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
  showCreateDialog = false;
  activity: Activity | null = null;
  isCurrentUserPresident = false;
  showAddMemberDialog = false;
  selectedCommitteeId: number | null = null;
  currentUserId: number | null = null;
  showEditDialog = false;
  selectedCommitteeForEdit: any = null;
  showDeleteMemberDialog = false;
  selectedCommitteeForDelete: number | null = null;
  committeeMembers: any[] = [];
  loadingMembers = false;

  constructor(
    private committeeService: CommitteeService,
    private activityService: ActivityService,
    private tokenStorage: TokenStorageService
  ) { }

  ngOnInit(): void {
    if (this.activityId) {
      this.loadActivityDetails();
      this.loadCommittees();
      this.getCurrentUserId();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadActivityDetails(): void {
    this.activityService.getActivityById(this.activityId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (activity: Activity) => {
        this.activity = activity;
        this.checkIfCurrentUserIsPresident();
      },
      error: (err) => {
        console.error('Error loading activity details:', err);
      }
    });
  }

  checkIfCurrentUserIsPresident(): void {
    const currentUser = this.tokenStorage.getUser();
    if (currentUser && this.activity) {
      this.isCurrentUserPresident = currentUser.id === this.activity.presidentId;
    }
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

  getMemberCountColor(count: number): { '--badge-color': string; '--badge-color-dark': string } {
    const colors = [
      { main: '#3b82f6', dark: '#2563eb' },  // blue
      { main: '#8b5cf6', dark: '#7c3aed' },  // purple
      { main: '#ec4899', dark: '#db2777' },  // pink
      { main: '#10b981', dark: '#059669' }   // green
    ];
    const color = colors[count % colors.length];
    return {
      '--badge-color': color.main,
      '--badge-color-dark': color.dark
    };
  }

  onCommitteeClick(committee: any): void {
    // Add any click functionality here
    console.log('Committee clicked:', committee.name);
  }

  openCreateDialog(): void {
    this.showCreateDialog = true;
  }

  closeCreateDialog(): void {
    this.showCreateDialog = false;
  }

  onCreateCommittee(committeeData: any): void {
    this.committeeService.createCommittee(committeeData).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response) => {
        console.log('Committee created successfully:', response);
        this.closeCreateDialog();
        this.loadCommittees(); // Refresh the list
      },
      error: (err) => {
        console.error('Error creating committee:', err);
      }
    });
  }

  getCurrentUserId(): void {
    const currentUser = this.tokenStorage.getUser();
    if (currentUser) {
      this.currentUserId = currentUser.id;
    }
  }

  isCurrentUserCommitteeHead(committee: any): boolean {
    return this.currentUserId !== null && committee.head?.id === this.currentUserId;
  }

  canDeleteMember(committee: any): boolean {
    // Only committee heads can delete members from their own committees
    return this.isCurrentUserCommitteeHead(committee);
  }

  openAddMemberDialog(committeeId: number): void {
    this.selectedCommitteeId = committeeId;
    this.showAddMemberDialog = true;
  }

  closeAddMemberDialog(): void {
    this.showAddMemberDialog = false;
    this.selectedCommitteeId = null;
  }

  onMemberAdded(): void {
    this.closeAddMemberDialog();
    this.loadCommittees(); // Refresh the committees to update member counts
  }

  openEditDialog(committee: any): void {
    this.selectedCommitteeForEdit = committee;
    this.showEditDialog = true;
  }

  closeEditDialog(): void {
    this.showEditDialog = false;
    this.selectedCommitteeForEdit = null;
  }

  onCommitteeUpdated(): void {
    this.closeEditDialog();
    this.loadCommittees(); // Refresh the committees
  }

  openDeleteMemberDialog(committeeId: number): void {
    this.selectedCommitteeForDelete = committeeId;
    this.showDeleteMemberDialog = true;
    this.loadCommitteeMembers(committeeId);
  }

  closeDeleteMemberDialog(): void {
    this.showDeleteMemberDialog = false;
    this.selectedCommitteeForDelete = null;
    this.committeeMembers = [];
  }

  loadCommitteeMembers(committeeId: number): void {
    this.loadingMembers = true;
    this.committeeService.getCommitteeMembers(committeeId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (members) => {
        this.committeeMembers = members;
        this.loadingMembers = false;
      },
      error: (err) => {
        console.error('Error loading committee members:', err);
        this.committeeMembers = [];
        this.loadingMembers = false;
      }
    });
  }

  deleteMemberFromCommittee(committeeId: number, memberId: number): void {
    if (confirm('Are you sure you want to remove this member from the committee?')) {
      this.committeeService.deleteMemberFromCommittee(committeeId, memberId).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response) => {
          console.log('Member removed successfully:', response);
          this.loadCommitteeMembers(this.selectedCommitteeForDelete!); // Refresh dialog member list
          this.loadCommittees(); // Refresh the committees to update member counts
        },
        error: (err) => {
          console.error('Error removing member:', err);
          alert('Failed to remove member from committee. Please try again.');
        }
      });
    }
  }
}
