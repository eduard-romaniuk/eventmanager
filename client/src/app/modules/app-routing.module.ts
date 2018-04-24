import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { HelloComponent } 		from '../components/hello/hello.component';
import { HomeComponent } 		from '../components/home/home.component';
import { AuthService } 			from '../services/auth.service';
import { CreateEventComponent } from '../components/createEvent/createEvent.component'

@Injectable()
class OnlyLoggedInUsersGuard implements CanActivate {
  constructor(private auth: AuthService) {};

  canActivate() {
    return this.auth.authenticated;
  }
}

@Injectable()
class OnlyAnonymousUsersGuard implements CanActivate {
  constructor(private auth: AuthService) {};

  canActivate() {
    return !this.auth.authenticated;
  }
}

const routes: Routes = [
  { path: '', redirectTo: '/hello', pathMatch: 'full' },
  { path: 'hello',
  	component: HelloComponent,
  	canActivate: [OnlyAnonymousUsersGuard]},
  { path: 'home',
    component: HomeComponent,
    canActivate: [OnlyLoggedInUsersGuard] },
  { path: 'createEvent',
    component: CreateEventComponent,
    canActivate: [OnlyAnonymousUsersGuard]}, //TODO: change to OnlyLoggedInUsersGuard
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ OnlyAnonymousUsersGuard, OnlyLoggedInUsersGuard ]
})
export class AppRoutingModule {}
