import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environments } from '../../../enviroments/enviroment';
import { TokenStorageService } from './token-storage.service';
import { CommitteeResponseDTO, CommitteeMemberCount } from '../models/committee.model';

@Injectable({
  providedIn: 'root'
})
export class CommitteeService {
  private apiUrl = environments.apiUrl;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) { }

  private getHeaders(): HttpHeaders {
    const token = this.tokenStorage.getToken();

    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  getCommitteesByActivity(activityId: number): Observable<CommitteeResponseDTO[]> {
    const url = `${this.apiUrl}/committees/activity/${activityId}`;
    console.log('Making request to:', url);

    return this.http.get<CommitteeResponseDTO[]>(url, { headers: this.getHeaders() }).pipe(
      map(response => {
        console.log('=== ANGULAR HTTP RESPONSE ===');
        console.log('Angular received:', response);
        if (response && response.length > 0) {
          console.log('First committee in Angular:', response[0]);
          console.log('Name value:', response[0].name);
          console.log('Head value:', response[0].head);
        }
        return response;
      })
    );
  }

  getCommitteeById(id: number): Observable<CommitteeResponseDTO> {
    return this.http.get<CommitteeResponseDTO>(`${this.apiUrl}/committees/${id}`, { headers: this.getHeaders() });
  }

  countCommitteeMembers(committeeId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/committees/${committeeId}/members/count`);
  }

  getCommitteeMembers(committeeId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/committees/${committeeId}/members`);
  }
}
