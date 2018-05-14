import {Component, OnInit, Input} from '@angular/core';
import {Router} from '@angular/router';

import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {ToastService} from "../../services/toast.service";
import {AuthService} from "../../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {phoneLength, usernameExists} from "../../utils/validation-tools";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  @Input() user: User = new User();
  oldLogin: string;
  password: string;

  form: FormGroup;
  savingChanges = false;
  error = false;

  constructor(private auth: AuthService,
              private router: Router,
              private userService: UserService,
              private formBuilder: FormBuilder,
              private toast: ToastService) {
  }

  ngOnInit() {
    this.oldLogin = this.auth.getSessionLogin();
    this.password = this.auth.getSessionPassword();

    this.form = this.formBuilder.group({
        editUserLogin: [this.user.login, [Validators.required,
          Validators.pattern('^[a-zA-Z0-9_]*$'),
          Validators.minLength(3),
          Validators.maxLength(30)],
          usernameExists(this.userService, this.oldLogin)
        ],
        editUserName: [this.user.name, [Validators.required]],
        editUserSurName: [this.user.surName, [Validators.required]],
        editUserPhone: [this.user.phone, [
          Validators.pattern('^[0-9-+ ()]*$')]]
      }, {validator: phoneLength('editUserPhone')}
    );
  }

  goHome() {
    this.savingChanges = false;
    this.auth.current_user.subscribe(
      current_user => {
        this.router.navigate(['users', current_user.id]);
        this.toast.success('Info successfully updated');
      });
  }

  save() {
    this.error = false;
    this.savingChanges = true;

    let loginChanged = this.user.login != this.oldLogin;
    this.userService.updateUser(this.user)
      .subscribe(response => {
          if (loginChanged) {
            this.auth.authenticate({
              login: this.user.login,
              password: this.password
            }, () => {
              this.goHome();
            });
          } else {
            this.goHome();
          }
        }, error => {
          console.log("Error - " + error);
          this.savingChanges = false;
          this.error = true;
        }
      );
  }

}
