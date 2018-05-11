import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../model/user";
import {AuthService} from "../../../services/auth.service";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {ToastService} from "../../../services/toast.service";
import {passConfirm} from "../../../utils/validation-tools";

@Component({
  selector: 'app-user-edit-password',
  templateUrl: './user-edit-password.component.html',
  styleUrls: ['./user-edit-password.component.css']
})
export class UserEditPasswordComponent implements OnInit {

  form: FormGroup;
  user: User = new User();
  formContent = {currentPassword: '', newPassword: '', confirmNewPassword: ''};

  savingChanges = false;
  wrongPasswordError = false;
  error = false;

  constructor(private auth: AuthService,
              private userService: UserService,
              private router: Router,
              private formBuilder: FormBuilder,
              private toast: ToastService) {
      this.auth.current_user.subscribe(
        current_user => {
          this.user = current_user;
        });
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
        currentPassword: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9]*$') ]],
        newPassword: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9]*$') ]],
        confirmNewPassword: ['', [ Validators.required ]]},
      {validator: passConfirm('newPassword', 'confirmNewPassword')});
  }

  save() {
    this.wrongPasswordError = false;
    this.error = false;
    this.savingChanges = true;

    this.userService.updateUserPassword(this.user.id, this.formContent.currentPassword, this.formContent.newPassword)
      .subscribe(response => {
          this.auth.authenticate({
            login: this.user.login,
            password: this.formContent.newPassword
          }, () => {
            this.savingChanges = false;
            this.auth.current_user.subscribe(
              current_user => {
                this.router.navigate(['users', current_user.id]);
                this.toast.success('Password successfully updated');
              });
          });
        }, error => {
          if (error.status === 409) {
            this.savingChanges = false;
            this.wrongPasswordError = true;
          } else {
            this.savingChanges = false;
            this.error = true;
          }
        }
      );
  }

}
