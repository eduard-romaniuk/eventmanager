import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { HelloComponent } 		from './hello/hello.component';
import { HomeComponent } 		from './home/home.component';
import { AuthService } 			from './auth.service';
import { CreateEventComponent } from './createEvent/createEvent.component'

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
