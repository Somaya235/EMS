import { StudentActivity } from './student-activity.model';
import { Committee } from './committee.model';

export interface Event {
  id?: number;
  title: string;
  description: string;
  startAt: string;
  endAt: string;
  activity?: StudentActivity;
  committee?: Committee;
  location?: string;
  status?: string;
  tags?: string[];
  bannerImage?: string;
  eventColor?: string;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}
