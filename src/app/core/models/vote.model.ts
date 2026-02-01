import { User } from './user.model';
import { PollOption } from './poll-option.model';

export interface Vote {
  id?: number;
  pollOption?: PollOption;
  user?: User;
  createdAt?: string;
}
