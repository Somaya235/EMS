import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { Activity, ActivityCategory } from '../../../core/models/activity.model';

interface ActivityCreateRequest {
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
    const endpoint = category ? `/student-activities?category=${category}` : '/student-activities';
    return this.apiService.get<Activity[]>(endpoint);
  }

  getActivityById(id: number): Observable<Activity> {
    return this.apiService.get<Activity>(`/student-activities/${id}`);
  }

  createActivity(activity: ActivityCreateRequest): Observable<Activity> {
    return this.apiService.post<Activity>('/admin/student-activities', activity);
  }

  updateActivity(id: number, activity: Partial<Activity>): Observable<Activity> {
    return this.apiService.put<Activity>(`/student-activities/${id}`, activity);
  }

  deleteActivity(id: number): Observable<void> {
    return this.apiService.delete<void>(`/student-activities/${id}`);
  }

  getActivityCategories(): Observable<ActivityCategory[]> {
    return this.apiService.get<ActivityCategory[]>('/student-activities/categories');
  }

  joinActivity(activityId: number): Observable<void> {
    return this.apiService.post<void>(`/student-activities/${activityId}/join`, {});
  }

  leaveActivity(activityId: number): Observable<void> {
    return this.apiService.post<void>(`/student-activities/${activityId}/leave`, {});
  }

  getActivityMembers(activityId: number): Observable<any[]> {
    return this.apiService.get<any[]>(`/student-activities/${activityId}/members`);
  }
}
