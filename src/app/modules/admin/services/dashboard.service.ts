import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { DashboardStats, Activity, Event, Poll } from '../../../core/models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private apiService: ApiService) { }

 

  getStudentActivitiesCount(): Observable<number> {
    return this.apiService.get<number>('/student-activities/count');
  }

  getUpcomingEvents(): Observable<Event[]> {
    return this.apiService.get<Event[]>('/dashboard/events/upcoming');
  }

  getActivePolls(): Observable<Poll[]> {
    return this.apiService.get<Poll[]>('/dashboard/polls/active');
  }
}
