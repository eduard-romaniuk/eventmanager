import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private auth: AuthService,
              private router: Router) {
    if(this.auth.authenticated){
      this.auth.current_user.subscribe(
        current_user => {
          console.log(current_user);
          this.router.navigate(['users', current_user.id]);
        });
    } else {
      this.router.navigate(['hello']);
    }
  }

}

