import { Poll } from './poll.model';

export interface PollOption {
  id?: number;
  optionText: string;
  poll?: Poll;
}
