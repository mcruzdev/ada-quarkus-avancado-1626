import { Routes } from '@angular/router';
import { CourseListComponent } from './components/course-list/course-list.component';
import { CourseFormComponent } from './components/course-form/course-form.component';
import { CourseDetailComponent } from './components/course-detail/course-detail.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { TransferComponent } from './components/transfer/transfer.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/courses',
    pathMatch: 'full'
  },
  {
    path: 'courses',
    component: CourseListComponent
  },
  {
    path: 'courses/new',
    component: CourseFormComponent
  },
  {
    path: 'courses/:id',
    component: CourseDetailComponent
  },
  {
    path: 'courses/:id/edit',
    component: CourseFormComponent
  },
  {
    path: 'transfer',
    component: TransferComponent
  },
  {
    path: 'users/new',
    component: UserFormComponent
  },
  {
    path: '**',
    redirectTo: '/courses'
  }
];
