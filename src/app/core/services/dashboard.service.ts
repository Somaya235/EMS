import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { DashboardStats, Activity, Event, Poll } from '../models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private apiService: ApiService) {}

  getDashboardStats(): Observable<DashboardStats> {
    return this.apiService.get<DashboardStats>('/dashboard/stats');
  }

  getRecentActivities(): Observable<Activity[]> {
    return this.apiService.get<Activity[]>('/dashboard/activities/recent');
  }

  getUpcomingEvents(): Observable<Event[]> {
    return this.apiService.get<Event[]>('/dashboard/events/upcoming');
  }

  getActivePolls(): Observable<Poll[]> {
    return this.apiService.get<Poll[]>('/dashboard/polls/active');
  }
}
