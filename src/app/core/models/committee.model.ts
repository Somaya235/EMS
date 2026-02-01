import { User } from './user.model';
import { StudentActivity } from './student-activity.model';

export interface Committee {
  id?: number;
  name: string;
  description: string;
  activity?: StudentActivity;
  director?: User;
  head?: User;
  members?: User[];
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}

export interface CommitteeResponseDTO {
  id: number;
  name: string;
  description: string;
  activity?: {
    id: number;
    name: string;
    description: string;
    category: string;
    presidentId: number;
    presidentName: string;
  };
  head?: {
    id: number;
    fullName: string;
    email: string;
    enabled: boolean;
    grade?: string;
    major?: string;
    phoneNumber?: string;
    nationalNumber?: string;
    dateOfBirth?: string;
    cvAttachment?: string;
    profileImage?: string;
    collageId?: string;
    roles: string[];
    createdAt: string;
    updatedAt: string;
  };
  director?: {
    id: number;
    fullName: string;
    email: string;
    enabled: boolean;
    grade?: string;
    major?: string;
    phoneNumber?: string;
    nationalNumber?: string;
    dateOfBirth?: string;
    cvAttachment?: string;
    profileImage?: string;
    collageId?: string;
    roles: string[];
    createdAt: string;
    updatedAt: string;
  };
  members: any[];
  createdAt: string;
}

export interface CommitteeMemberCount {
  committeeId: number;
  memberCount: number;
}
