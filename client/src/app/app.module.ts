import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { HttpClientModule, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS, HttpHeaders } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AuthService }            from './auth.service';
import { AppComponent }          from './app.component';
import { LoginComponent }        from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HelloComponent }        from './hello/hello.component';
import { AppRoutingModule }      from './app-routing.module';
import { HomeComponent }         from './home/home.component';
import { LoggerService } from './logger.service';
import { UserComponent } from './user/user.component';
import { UserListComponent } from './user/user-list.component';
import { UserService } from './user/user.service';
import { UserEditComponent } from './user/user-edit.component';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    req = req.clone({
       setHeaders: {
          Authorization: sessionStorage.getItem('authToken')
       }
    });
    return next.handle(req);
  }
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HelloComponent,
    HomeComponent,
    UserComponent,
    UserListComponent,
    UserEditComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthService,
    { provide: HTTP_INTERCEPTORS,
      useClass: AuthenticationInterceptor,
      multi: true},
    LoggerService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
