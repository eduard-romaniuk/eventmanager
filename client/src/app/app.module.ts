import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AuthService }            from './auth.service';
import { AppComponent }          from './app.component';
import { LoginComponent }        from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HelloComponent }        from './hello/hello.component';
import { AppRoutingModule }      from './app-routing.module';
import { HomeComponent }         from './home/home.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HelloComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
