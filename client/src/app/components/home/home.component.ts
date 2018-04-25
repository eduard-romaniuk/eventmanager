import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  user: User = new User();

  constructor(private http: HttpClient, private auth: AuthService) {
    this.auth.current_user.subscribe(response => this.user = response);
  }
}
