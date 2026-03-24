import { Routes } from '@angular/router';
import { CourseListComponent } from './components/course-list/course-list.component';
import { CourseFormComponent } from './components/course-form/course-form.component';
import { CourseDetailComponent } from './components/course-detail/course-detail.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'courses',
    component: CourseListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'courses/new',
    component: CourseFormComponent,
    canActivate: [authGuard]
  },
  {
    path: 'courses/:id',
    component: CourseDetailComponent,
    canActivate: [authGuard]
  },
  {
    path: 'courses/:id/edit',
    component: CourseFormComponent,
    canActivate: [authGuard]
  },
  {
    path: 'transfer',
    component: TransferComponent,
    canActivate: [authGuard]
  },
  {
    path: 'users/new',
    component: UserFormComponent,
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
