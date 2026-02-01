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
