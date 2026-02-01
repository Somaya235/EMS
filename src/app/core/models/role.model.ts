export interface Role {
  id: number;
  name: string;

  createdAt: Date;
  updatedAt: Date;
}

export enum RoleType {
  SUPER_ADMIN = 'SUPER_ADMIN',
  WEB_MANAGER = 'WEB_MANAGER',
  PRESIDENT = 'PRESIDENT',
  DIRECTOR = 'DIRECTOR',
  COMMITTEE_HEAD = 'COMMITTEE_HEAD',
  COMMITTEE_MEMBER = 'COMMITTEE_MEMBER',
  MEMBER = 'MEMBER'
}
