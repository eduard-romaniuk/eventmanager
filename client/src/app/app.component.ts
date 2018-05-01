import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { AuthService } from './services/auth.service';
import 'rxjs/add/operator/finally';
import {User} from "./model/user";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Event manager';
  user: User = new User();

  constructor(private auth: AuthService, private http: HttpClient, private router: Router) {
    this.auth.current_user.subscribe(
      current_user => {
        this.user = current_user;
      });
  }

  logout() {
    this.auth.logout(() => this.router.navigateByUrl('/hello'));
  }

  authenticated() {
    return this.auth.authenticated;
  }

  createEvent() {
    this.router.navigate(['/event/create', this.auth]);
  }
}
