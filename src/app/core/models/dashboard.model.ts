export interface DashboardStats {
  totalActivities: number;
  totalEvents: number;
  upcomingEvents: number;
  activePolls: number;
}

export interface Activity {
  id: number;
  name: string;
  memberCount: number;
  joinDate: string;
}

export interface Event {
  id: number;
  name: string;
  club: string;
  date: Date;
  time: string;
  location: string;
}

export interface Poll {
  id: number;
  question: string;
  isPublic: boolean;
  totalVotes: number;
  endDate: Date;
}
