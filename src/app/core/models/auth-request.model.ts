import { User } from './user.model';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  grade: number;
  phoneNumber: string;
  nationalNumber: string;
  dateOfBirth: Date;
  collageId: string;
  roles: string[];
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

export interface VerifyOtpRequest {
  email: string;
  otp: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface JwtResponse {
  accessToken?: string;
  refreshToken?: string;
  token?: string;
  tokenType?: string;
  expiresIn?: number;
  user?: User;
  id?: number;
  email?: string;
  fullName?: string;
  roles?: string[];
  type?: string;
}

export interface MessageResponse {
  message: string;
}
