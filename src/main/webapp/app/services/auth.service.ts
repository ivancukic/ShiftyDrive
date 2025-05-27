import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, map, catchError } from 'rxjs/operators';
import { ApplicationConfigService } from '../core/config/application-config.service'; 
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

const TOKEN_KEY = 'auth_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

interface JWTPayload {
  exp: number;
}

interface LoginResponse {
  token: string;
  refreshToken: string;
}

interface LoginDto {
}
interface RegisterDto {
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl: string;
  private loggedInSubject = new BehaviorSubject<boolean>(this.hasToken());

  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private router: Router 
  ) {
    this.baseUrl = `${this.applicationConfigService.getEndpointPrefix('auth')}`;
  }

  login(data: LoginDto): Observable<LoginResponse> {
    this.logout(); 
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, data).pipe(
      tap((response) => {
        this.setToken(response.token, response.refreshToken);
      })
    );
  }

  register(data: RegisterDto): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, data);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    this.loggedInSubject.next(false);
    this.router.navigate(['/login']); 
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedInSubject.asObservable().pipe(
      map(() => this.hasToken()) 
    );
  }

  private hasToken(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded = jwtDecode<JWTPayload>(token);
      const now = Math.floor(Date.now() / 1000);
      return decoded.exp > now;
    } catch (error) {
      return false; 
    }
  }

  getCurrentUser(): JWTPayload | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded = jwtDecode<JWTPayload>(token);
      return decoded;
    } catch (err) {
      console.error('Invalid token:', err);
      return null;
    }
  }

  refreshToken(): Observable<string | null> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      this.logout();
      return of(null);
    }

    return this.http
      .post<LoginResponse>(`${this.baseUrl}/refresh-token`, { refreshToken })
      .pipe(
        tap((response) => {
          if (response) {
            this.setToken(response.token, response.refreshToken);
          }
        }),
        map((response) => (response ? response.token : null)),
        catchError((err) => {
          this.logout();
          return of(null); 
        })
      );
  }

  setToken(token: string, refreshToken?: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    if (refreshToken) {
      localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    }
    this.loggedInSubject.next(true);
  }
}