import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-recover-password',
  templateUrl: './recover-password.component.html',
  styleUrls: ['./recover-password.component.css']
})
export class RecoverPasswordComponent implements OnInit {

  form: FormGroup;
  login = '';

  constructor(private auth: AuthService, private users: UserService,
    private router: Router, private formBuilder: FormBuilder, private toast: ToastService) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      login: ['', [ Validators.required,
        Validators.pattern('^[a-zA-Z0-9_]*$'),
        Validators.minLength(3),
        Validators.maxLength(30) ],
        this.usernameNotExists.bind(this)]
    });
  }

  send() {
    this.auth.recoverPassword(this.login).subscribe(response => {
      this.router.navigateByUrl('home');
      this.toast.info('Check your email');
    });
  }

  usernameNotExists(control: AbstractControl) {
    return this.users.isUsernameExists(control.value).map(response => {return response ? null : { notexists: true }})
  }

}
