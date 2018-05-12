import {Component, OnInit} from '@angular/core';
import {User} from "../../../model/user";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent implements OnInit {

  user: User = new User();

  selectedEditInfo = true;
  selectedEditImage = false;
  selectedEditPassword = false;

  constructor(private router: Router,
              private auth: AuthService) {
  }

  ngOnInit() {
    this.auth.current_user.subscribe(
      current_user => {
        this.user = current_user;
      });
  }

  showEditInfo(): void {
    this.selectedEditInfo = true;
    this.selectedEditImage = false;
    this.selectedEditPassword = false;
  }

  showEditImage(): void {
    this.selectedEditInfo = false;
    this.selectedEditImage = true;
    this.selectedEditPassword = false;
  };

  showEditPassword(): void {
    this.selectedEditInfo = false;
    this.selectedEditImage = false;
    this.selectedEditPassword = true;
  };

  goBack(): void {
    this.router.navigate(['/home']);
  };

}
