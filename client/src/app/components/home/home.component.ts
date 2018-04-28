import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import {User} from "../../model/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  user: User = new User();

  constructor(private auth: AuthService,
              private router: Router) {
    this.auth.current_user.subscribe(
      current_user => {
        console.log(current_user);
        this.user = current_user;
      });
  }

  goToEditUserPage(user: User): void {
    this.router.navigate(['users', user.id, 'edit']);
  }

  goToEditImagePage(user: User): void {
    this.router.navigate(['users', user.id, 'updateImage']);
  };

}
