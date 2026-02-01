import { Event } from './event.model';

export interface Poll {
  id?: number;
  title: string;
  description: string;
  isPublic: boolean;
  tags?: string[];
  event?: Event;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}
