import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/users';

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  createUser(request: UserRequest): Observable<User> {
    return this.http.post<User>(this.apiUrl, request);
  }

  updateUser(id: number, request: UserRequest): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, request);
  }

  getUserBalance(id: number): Observable<{ userId: number; balance: number }> {
    return this.http.get<{ userId: number; balance: number }>(`${this.apiUrl}/${id}/balance`);
  }
}

// Made with Bob
