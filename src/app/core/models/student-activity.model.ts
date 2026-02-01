import { User } from './user.model';

export interface StudentActivity {
  id?: number;
  name: string;
  description: string;
  category: string;
  president?: User;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}
