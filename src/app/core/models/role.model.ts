export interface Role {
  id: number;
  name: string;
  description?: string;
  permissions?: string[];
  createdAt: Date;
  updatedAt: Date;
}

export enum RoleType {
  ADMIN = 'ADMIN',
  USER = 'USER',
  MODERATOR = 'MODERATOR',
  ORGANIZER = 'ORGANIZER'
}
