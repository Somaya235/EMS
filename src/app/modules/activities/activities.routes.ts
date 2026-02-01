import { Routes } from '@angular/router';
import { ActivityListComponent } from './components/activity-list/activity-list.component';

export const ACTIVITIES_ROUTES: Routes = [
  {
    path: '',
    component: ActivityListComponent
  }
];
