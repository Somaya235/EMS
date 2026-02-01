import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Activity, ActivityCategory } from '../models/activity.model';

export interface ActivityCreateRequest {
  name: string;
  description: string;
  category: string;
  presidentId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  constructor(private apiService: ApiService) { }

  getActivities(category?: string): Observable<Activity[]> {
    const endpoint = category ? `/activities?category=${category}` : '/activities';
    return this.apiService.get<Activity[]>(endpoint);
  }

  getActivityById(id: number): Observable<Activity> {
    return this.apiService.get<Activity>(`/activities/${id}`);
  }

  createActivity(activity: ActivityCreateRequest): Observable<any> {
    return this.apiService.post<any>('/admin/student-activities', activity);
  }

  updateActivity(id: number, activity: Partial<Activity>): Observable<Activity> {
    return this.apiService.put<Activity>(`/activities/${id}`, activity);
  }

  deleteActivity(id: number): Observable<void> {
    return this.apiService.delete<void>(`/activities/${id}`);
  }

  getActivityCategories(): Observable<ActivityCategory[]> {
    return this.apiService.get<ActivityCategory[]>('/activities/categories');
  }

  joinActivity(activityId: number): Observable<void> {
    return this.apiService.post<void>(`/activities/${activityId}/join`, {});
  }

  leaveActivity(activityId: number): Observable<void> {
    return this.apiService.post<void>(`/activities/${activityId}/leave`, {});
  }

  getActivityMembers(activityId: number): Observable<any[]> {
    return this.apiService.get<any[]>(`/activities/${activityId}/members`);
  }
}
