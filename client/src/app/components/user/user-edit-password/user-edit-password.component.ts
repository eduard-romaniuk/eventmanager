import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../model/user";
import {AuthService} from "../../../services/auth.service";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";
import {ToastService} from "../../../services/toast.service";

@Component({
  selector: 'app-user-edit-password',
  templateUrl: './user-edit-password.component.html',
  styleUrls: ['./user-edit-password.component.css']
})
export class UserEditPasswordComponent implements OnInit {

  form: FormGroup;
  user: User = new User();
  formContent = {currentPassword: '', newPassword: ''};

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
    });
  }

  save() {
    this.user.password = this.formContent.newPassword;

    this.userService.updateUserPassword(this.user).subscribe(response => {
      this.auth.setSessionAuthToken(this.user.login, this.user.password);
      this.auth.current_user.subscribe(
        current_user => {
          console.log(current_user);
          this.router.navigate(['users', current_user.id]);
          this.toast.success('Password successfully updated');
        });
    });
  }

}
