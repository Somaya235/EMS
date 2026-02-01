import { User } from './user.model';

export interface OtpCode {
  id?: number;
  user: User;
  code: string;
  expiryDate: string;
  verified: boolean;
  createdAt?: string;
}

export interface RefreshToken {
  id?: number;
  user: User;
  token: string;
  expiryDate: string;
}
