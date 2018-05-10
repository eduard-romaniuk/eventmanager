import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-recover-login',
  templateUrl: './recover-login.component.html',
  styleUrls: ['./recover-login.component.css']
})
export class RecoverLoginComponent implements OnInit {

  form: FormGroup;
  email = '';

  constructor(private router: Router, private formBuilder: FormBuilder,
    private users: UserService, private auth: AuthService, private toast: ToastService) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      email: ['', [ Validators.required, Validators.email ], this.emailNotExists.bind(this)]
    });
  }

  send() {
    this.auth.recoverLogin(this.email).subscribe(response => {
      this.router.navigateByUrl('/home');
      this.toast.info('Check your email');
    });
  }

  emailNotExists(control: AbstractControl) {
    return this.users.isEmailExists(control.value).map(response => {return response ? null : { notexists: true };})
  }

}
