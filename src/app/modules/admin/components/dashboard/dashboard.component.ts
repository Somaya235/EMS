import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { User } from '../../../../core/models/user.model';
import { AuthService } from '../../../../core/services/auth.service';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardStats, Event } from '../../../../core/models/dashboard.model';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { catchError } from 'rxjs/operators';
import { of, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, DatePipe, HeaderComponent],
    templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit, OnDestroy {
    currentUser: User | null = null;
    dashboardStats: DashboardStats = {
        totalActivities: 0,
        totalEvents: 0,
        upcomingEvents: 0,
        activePolls: 0
    };
    studentActivitiesCount: number = 0;
    upcomingEventsList: Event[] = [];
    loading = true;
    error: string | null = null;
    private destroy$ = new Subject<void>();

    constructor(
        private authService: AuthService,
        private dashboardService: DashboardService
    ) { }

    ngOnInit(): void {
        this.loadCurrentUser();
        this.loadDashboardData();
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    loadCurrentUser(): void {
        this.authService.currentUser$.pipe(
            takeUntil(this.destroy$)
        ).subscribe(user => {
            this.currentUser = user;
        });
    }

    loadDashboardData(): void {
        this.loading = true;
        this.error = null;

        // Load all dashboard data in parallel
      

        // Load student activities count
        this.dashboardService.getStudentActivitiesCount().pipe(
            catchError(error => {
                console.error('Error loading student activities count:', error);
                return of(0);
            })
        ).subscribe(count => {
            this.studentActivitiesCount = count;
        });

        this.dashboardService.getUpcomingEvents().pipe(
            catchError(error => {
                console.error('Error loading upcoming events:', error);
                this.error = 'Failed to load upcoming events';
                return of([]);
            })
        ).subscribe(events => {
            this.upcomingEventsList = events;
            this.loading = false;
        });
    }

    private getDefaultStats(): DashboardStats {
        return {
            totalActivities: 0,
            totalEvents: 0,
            upcomingEvents: 0,
            activePolls: 0
        };
    }
}