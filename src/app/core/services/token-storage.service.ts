import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const REFRESH_TOKEN_KEY = 'refresh-token';
const USER_KEY = 'auth-user';
const TOKEN_EXPIRY_KEY = 'auth-token-expiry';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  constructor() { }

  signOut(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);

    // Set token expiry (1 hour from now)
    const expiryTime = new Date().getTime() + (60 * 60 * 1000);
    window.sessionStorage.setItem(TOKEN_EXPIRY_KEY, expiryTime.toString());
  }

  public saveRefreshToken(token: string): void {
    window.sessionStorage.removeItem(REFRESH_TOKEN_KEY);
    window.sessionStorage.setItem(REFRESH_TOKEN_KEY, token);
  }

  public getToken(): string | null {
    const token = window.sessionStorage.getItem(TOKEN_KEY);
    const expiryTime = window.sessionStorage.getItem(TOKEN_EXPIRY_KEY);

    // Check if token has expired
    if (token && expiryTime) {
      const now = new Date().getTime();
      if (now > parseInt(expiryTime)) {
        this.signOut(); // Clear expired token
        return null;
      }
    }

    return token;
  }

  public getRefreshToken(): string | null {
    return window.sessionStorage.getItem(REFRESH_TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }
    return null;
  }
}
