import { Committee } from './committee.model';
import { User } from './user.model';

export interface CommitteeMemberId {
  committeeId: number;
  committeeMemberId: number;
}

export interface CommitteeMember {
  id: CommitteeMemberId;
  committee: Committee;
  member: User;
  joinedAt?: string;
}
