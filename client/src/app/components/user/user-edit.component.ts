import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../../model/user';
import { UserService } from '../../services/user.service';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnDestroy {

  user: User = new User();
  sub: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.userService.getUserById(id).subscribe((user: any) => {
          if (user) {
            this.user = user;
          } else {
            console.log(`User with id '${id}' not found!`);
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  save() {
    this.userService.updateUser(this.user).subscribe((user: any) => {
      this.router.navigate(['users', user.id]);
    }, error => console.error(error));
    // this.userService.updateUser(this.user);
    // this.router.navigate(['users', this.user.id]);
  }

}