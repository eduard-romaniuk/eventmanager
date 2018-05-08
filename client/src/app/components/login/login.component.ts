import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

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

  constructor(private auth: AuthService, private router: Router, private formBuilder: FormBuilder) {
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
        this.form.reset();
        this.credentials.password = '';
        this.credentials.login = '';
        this.error = false;
        this.loading = false;

        this.auth.current_user.subscribe(
          current_user => {
            console.log(current_user);
            this.router.navigate(['users', current_user.id]);
          });
      },
      () => {
        this.error = true,
        this.loading = false
      });
  }

}
