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

  constructor(private auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout(() => this.router.navigateByUrl('/hello'));
  }

  authenticated() {
    return this.auth.authenticated;
  }
}
