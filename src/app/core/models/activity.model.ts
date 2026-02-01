export interface Activity {
  id: number;
  name: string;
  description: string;
  category: string;
  memberCount: number;
  joinDate: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  isActive: boolean;
  maxMembers?: number;
  meetingSchedule?: string;
  requirements?: string[];
  imageUrl?: string;
}

export interface ActivityCategory {
  id: number;
  name: string;
  description: string;
  color: string;
}
