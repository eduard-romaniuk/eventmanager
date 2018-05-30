import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { ToastService } from '../../services/toast.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { passConfirm } from '../../utils/validation-tools';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  token = '';
  form: FormGroup;
  credentials = {login: '', password: ''};
  loading = false;
  error = false;

  constructor(private route: ActivatedRoute, private auth: AuthService, private users: UserService,
    private router: Router, private formBuilder: FormBuilder, private toast: ToastService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.token = params['token'];
      if(this.token) {
        this.form = this.formBuilder.group({
          login: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9_]*$') ]],
          password: ['', [ Validators.required,
            Validators.pattern('^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)*$'),
            Validators.minLength(6),
            Validators.maxLength(30) ]],
          confirmPassword: ['', [ Validators.required ]]},
          {validator: passConfirm('password', 'confirmPassword')}
        );
      } else {
        this.router.navigate(['home']);
        this.toast.error('Invalid recovery link');
      }
    });

  }

  change() {
    this.loading = true;
    this.error = false;
    this.auth.updateUserPassword(this.credentials.login, this.token, this.credentials.password)
    .subscribe(response => {
      this.auth.authenticate(this.credentials, false, () => {
        this.form.reset();
        this.credentials.password = '';
        this.credentials.login = '';
        this.loading = false;
        this.error = false;
        this.auth.current_user.subscribe(
          current_user => {
            console.log(current_user);
            this.router.navigate(['users', current_user.id]);
            this.toast.info('Password changed');
          });
        });
      }, error => {
        this.error = true;
        this.loading = false;
      });
  }

}
