import { Routes } from '@angular/router';
import { StudentListComponent } from './components/student-list/student-list.component';
import { StudentProfile } from './components/student-profile/student-profile';

export const STUDENTS_ROUTES: Routes = [
    {
        path: '',
        component: StudentListComponent,
        title: 'Students - EMS'
    },
    {
        path: 'profile',
        component: StudentProfile,
        title: 'My Profile - EMS'
    },
    {
        path: ':id',
        component: StudentProfile,
        title: 'Student Profile - EMS'
    }
];
