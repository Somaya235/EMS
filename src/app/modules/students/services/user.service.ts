import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { User } from '../../../core/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private apiService: ApiService) { }

  getStudents(): Observable<User[]> {
    return this.apiService.get<User[]>('/admin/students');
  }

  getUserById(id: number): Observable<User> {
    return this.apiService.get<User>(`/admin/students/${id}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.apiService.get<User[]>('/admin/users');
  }
}
