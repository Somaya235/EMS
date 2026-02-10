import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { LoginComponent } from './modules/auth/components/login/login.component';
import { RegisterComponent } from './modules/auth/components/register/register.component';
import { VerifyOtpComponent } from './modules/auth/components/verify-otp/verify-otp.component';
import { ForgotPasswordComponent } from './modules/auth/components/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './modules/auth/components/reset-password/reset-password.component';
import { DashboardComponent } from './modules/admin/components/dashboard/dashboard.component';
import { ActivityListComponent } from './modules/activities/components/activity-list/activity-list.component';
import { ActivityDetailsComponent } from './modules/activities/components/activity-details/activity-details.component';
import { StudentListComponent } from './modules/students/components/student-list/student-list.component';
import { StudentProfile } from './modules/students/components/student-profile/student-profile';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/auth/login',
        pathMatch: 'full'
    },
    {
        path: 'auth',
        children: [
            {
                path: 'login',
                component: LoginComponent,
                title: 'Login - EMS'
            },
            {
                path: 'register',
                component: RegisterComponent,
                title: 'Register - EMS'
            },
            {
                path: 'verify-otp',
                component: VerifyOtpComponent,
                title: 'Verify Email - EMS'
            },
            {
                path: 'forgot-password',
                component: ForgotPasswordComponent,
                title: 'Forgot Password - EMS'
            },
            {
                path: 'reset-password',
                component: ResetPasswordComponent,
                title: 'Reset Password - EMS'
            }
        ]
    },
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        title: 'Dashboard - EMS'
    },
    {
        path: 'activities',
        component: ActivityListComponent,
        canActivate: [AuthGuard],
        title: 'Activities - EMS'
    },
    {
        path: 'activities/:id',
        component: ActivityDetailsComponent,
        canActivate: [AuthGuard],
        title: 'Activity Details - EMS'
    },

    {
        path: 'students',
        canActivate: [AuthGuard, RoleGuard],
        children: [
            {
                path: '',
                component: StudentListComponent,
                title: 'Students - EMS'
            },
            {
                path: ':id',
                component: StudentProfile,
                title: 'Student Profile - EMS'
            }
        ]
    },

    {
        path: 'profile',
        component: StudentProfile,
        canActivate: [AuthGuard],
        title: 'My Profile - EMS'
    },

    {
        path: '**',
        redirectTo: '/auth/login'
    }
];
