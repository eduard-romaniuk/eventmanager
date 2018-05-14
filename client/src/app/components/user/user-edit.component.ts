import {Component, OnInit, Input} from '@angular/core';
import {Router} from '@angular/router';

import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {ToastService} from "../../services/toast.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  @Input() user: User = new User();
  oldLogin: String;
  password: String;

  constructor(private auth: AuthService,
              private router: Router,
              private userService: UserService,
              private toast: ToastService) {
  }

  ngOnInit() {
    this.oldLogin = this.auth.getSessionLogin();
    this.password = this.auth.getSessionPassword();
  }

  goHome() {
    //this.savingChanges = false;
    this.auth.current_user.subscribe(
      current_user => {
        this.router.navigate(['users', current_user.id]);
        this.toast.success('Info successfully updated');
      });
  }

  save() {
    let loginChanged = this.user.login != this.oldLogin;
    this.userService.updateUser(this.user)
      .subscribe(response => {
          if (loginChanged) {
            console.log("this.user.login - " + this.user.login);
            console.log("this.password - " + this.password);
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
          console.log("Error in update");
          // this.savingChanges = false;
          // this.error = true;
        }
      );
  }

}
