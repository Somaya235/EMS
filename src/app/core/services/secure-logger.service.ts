import { Injectable } from '@angular/core';
import { environments } from '../../../enviroments/enviroment';
@Injectable({
  providedIn: 'root'
})
export class SecureLoggerService {

  private isProduction(): boolean {
    return environments.production;
  }

  private maskSensitiveData(data: any): any {
    if (!data || typeof data !== 'object') {
      return data;
    }

    const sensitiveFields = ['token', 'accessToken', 'refreshToken', 'password', 'authorization'];
    const masked = { ...data };

    for (const key in masked) {
      if (sensitiveFields.some(field => key.toLowerCase().includes(field.toLowerCase()))) {
        masked[key] = '***MASKED***';
      } else if (typeof masked[key] === 'object' && masked[key] !== null) {
        masked[key] = this.maskSensitiveData(masked[key]);
      }
    }

    return masked;
  }

  log(message: string, data?: any): void {
    if (!this.isProduction()) {
      const safeData = data ? this.maskSensitiveData(data) : undefined;
      console.log(message, safeData);
    }
  }

  error(message: string, error?: any): void {
    const safeError = error ? this.maskSensitiveData(error) : undefined;
    console.error(message, safeError);
  }

  warn(message: string, data?: any): void {
    const safeData = data ? this.maskSensitiveData(data) : undefined;
    console.warn(message, safeData);
  }

  info(message: string, data?: any): void {
    if (!this.isProduction()) {
      const safeData = data ? this.maskSensitiveData(data) : undefined;
      console.info(message, safeData);
    }
  }
}
