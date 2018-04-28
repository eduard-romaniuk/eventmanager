import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import {HttpClient} from '@angular/common/http';
import { Router } from '@angular/router';
import { JQueryStatic } from 'jquery';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

declare var $:JQueryStatic;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  credentials = {login: '', password: ''};
  loading = false;
  error = false;

  constructor(private auth: AuthService, private http: HttpClient, private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      login: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9_]*$') ]],
      password: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9]*$') ]]
    });
  }

  login() {
    this.loading = true;
    this.error = false;

    this.auth.authenticate(this.credentials, () => {
        $('#signInModal').modal('hide');

        this.form.reset();
        this.credentials.password = '';
        this.credentials.login = '';
        this.error = false;
        this.loading = false;

        this.router.navigate(['home']);
      },
      () => {
        this.error = true,
          this.loading = false
      });
  }

}
