export interface User {
  id: number;
  fullName: string;
  email: string;
  passwordHash?: string; // Optional since it's not returned in DTO
  grade: string;
  phoneNumber?: string; // Make optional since it might be null
  enabled?: boolean;
  nationalNumber?: string; // Make optional
  dateOfBirth?: Date | string; // Allow string for API response
  cvAttachment?: string;
  profileImage?: string;
  collageId?: string; // Make optional
  roles: string[]; // Changed from Role[] to string[] to match backend DTO
  major?: string; // Added major field from backend DTO
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}

export interface Role {
  id?: number;
  name: string;
  createdAt?: string;
}


