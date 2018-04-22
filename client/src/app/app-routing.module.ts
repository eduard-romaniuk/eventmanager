import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate, Router } from '@angular/router';
import { HelloComponent } 		from './hello/hello.component';
import { HomeComponent } 		from './home/home.component';
import { AuthService } 			from './auth.service';

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
  { path: '**', redirectTo: '/hello', pathMatch: 'full'}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ OnlyLoggedInUsersGuard ]
})
export class AppRoutingModule {}