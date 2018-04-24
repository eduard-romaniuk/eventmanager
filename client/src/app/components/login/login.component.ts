import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { JQueryStatic } from 'jquery';

declare var $:JQueryStatic;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  login_error = false;

  constructor(private auth: AuthService, private http: HttpClient, private router: Router) {
  }

  login() {
    // TODO: Handle login error
    this.auth.authenticate(this.credentials, () => {
      $('#signInModal').modal('hide');
      this.router.navigateByUrl('/home');
    });
  }

}
