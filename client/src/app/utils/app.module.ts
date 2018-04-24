import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AuthService }            from '../services/auth.service';
import { AppComponent }          from './app.component';
import { LoginComponent }        from '../components/login/login.component';
import { RegistrationComponent } from '../components/registration/registration.component';
import { HelloComponent }        from '../components/hello/hello.component';
import { AppRoutingModule }      from '../modules/app-routing.module';
import { HomeComponent }         from '../components/home/home.component';
import { CreateEventComponent }   from '../components/createEvent/createEvent.component'
import {EventService} from "../services/event.service";


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HelloComponent,
    HomeComponent,
    CreateEventComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    AuthService,
    EventService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
