export const APP_CONSTANTS = {
  APP_NAME: 'EMS',
  VERSION: '1.0.0',
  API_TIMEOUT: 30000,
  REFRESH_TOKEN_INTERVAL: 15 * 60 * 1000, // 15 minutes
  MAX_FILE_SIZE: 5 * 1024 * 1024, // 5MB
  SUPPORTED_IMAGE_TYPES: ['image/jpeg', 'image/png', 'image/gif'],
  SUPPORTED_DOCUMENT_TYPES: ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
};

export const ROUTES = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    FORGOT_PASSWORD: '/auth/forgot-password',
    VERIFY_OTP: '/auth/verify-otp'
  },
  ADMIN: {
    DASHBOARD: '/admin/dashboard',
    USER_MANAGEMENT: '/admin/users',
    ROLE_MANAGEMENT: '/admin/roles',
    SYSTEM_SETTINGS: '/admin/settings'
  },
  EVENTS: {
    LIST: '/events',
    CREATE: '/events/create',
    DETAIL: '/events/:id',
    EDIT: '/events/:id/edit'
  },
  COMMITTEES: {
    LIST: '/committees',
    CREATE: '/committees/create',
    DETAIL: '/committees/:id',
    EDIT: '/committees/:id/edit'
  },
  PROFILE: {
    VIEW: '/profile',
    EDIT: '/profile/edit'
  }
};

export const ROLES = {
  ADMIN: 'ADMIN',
  USER: 'USER',
  MODERATOR: 'MODERATOR',
  ORGANIZER: 'ORGANIZER'
};
