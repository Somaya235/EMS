import { User } from './user.model';
import { StudentActivity } from './student-activity.model';

export interface ActivityDirectorId {
  activityId: number;
  directorId: number;
}

export interface ActivityDirector {
  id: ActivityDirectorId;
  activity: StudentActivity;
  director: User;
  jobDesc?: string;
  name?: string;
  assignedAt?: string;
}
