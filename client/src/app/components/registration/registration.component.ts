import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { JQueryStatic } from 'jquery';
import { FormGroup, FormBuilder, Validators, FormControl, AbstractControl } from '@angular/forms';
import { User } from '../../model/user';
import { passConfirm, boolean } from '../../utils/validation-tools';
import 'rxjs/add/operator/map';

declare var $:JQueryStatic;

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  form: FormGroup;
  loading = false;
  user: User = new User();

  constructor(private auth: AuthService, private users: UserService, private http: HttpClient, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      name: ['', [ Validators.required ]],
      surName: ['', [ Validators.required ]],
      sex: [null, [ boolean ]],
      email: ['', [ Validators.required, Validators.email ], this.emailExists.bind(this)],
      login: ['', [ Validators.required,
        Validators.pattern('^[a-zA-Z0-9_]*$'),
        Validators.minLength(3),
        Validators.maxLength(30) ],
        this.usernameExists.bind(this)],
      password: ['', [ Validators.required,
        Validators.pattern('^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)*$'),
        Validators.minLength(6),
        Validators.maxLength(30) ]],
      confirmPassword: ['', [ Validators.required ]]}, {validator: passConfirm});
  }

  registration(){
    console.log(this.user);
    // this.form.reset();
    this.auth.registration(this.user);
    $('#logTab').tab('show');
  }

  emailExists(control: AbstractControl) {
    return this.users.isEmailExists(control.value).map(response => {return response ? { exists: true } : null;})
  }

  usernameExists(control: AbstractControl) {
    return this.users.isUsernameExists(control.value).map(response => {return response ? { exists: true } : null;})
  }
}
