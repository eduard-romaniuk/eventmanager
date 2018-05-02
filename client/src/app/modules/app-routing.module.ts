import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate, Router } from '@angular/router';
import { HelloComponent } 		from '../components/hello/hello.component';
import { HomeComponent } 		  from '../components/home/home.component';
import { CreateEventComponent }       from '../components/createEvent/createEvent.component';
import { AuthService } 			  from '../services/auth.service';

import { UserComponent } from '../components/user/user.component';
import { UserListComponent } from '../components/user/user-list.component';
import { UserEditComponent } from '../components/user/user-edit.component';
import {ExportEventsPlanComponent} from "../components/export-events-plan/export-events-plan.component";

@Injectable()
class OnlyLoggedInUsersGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {};

  canActivate() {
    if (!this.auth.authenticated) {
      this.router.navigateByUrl('/hello');
      return false;
    }
    return true;
  }
}

const routes: Routes = [
  { path: '', redirectTo: '/hello', pathMatch: 'full' },
  { path: 'hello',
  	component: HelloComponent },
  { path: 'home',
    component: HomeComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'event',
    component: CreateEventComponent },
  { path: 'users',
    component: UserListComponent},
  { path: 'users/:id',
    component: UserComponent},
  { path: 'users/:id/edit',
    component: UserEditComponent},
  { path: 'event/export',
    component: ExportEventsPlanComponent},
  { path: '**', redirectTo: '/hello', pathMatch: 'full'}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ OnlyLoggedInUsersGuard ]
})
export class AppRoutingModule {}
