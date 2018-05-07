import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../../model/user';
import { UserService } from '../../services/user.service';
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  currentUser: User = new User();
  user: User = new User();

  userEvents: Event[];
  userFriends: User[];
  sub: Subscription;

  currentUserPage: boolean;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private auth: AuthService,
              private userService: UserService) { }

  ngOnInit() {
    this.auth.current_user.subscribe(
      current_user => {
        this.currentUser = current_user;

        this.sub = this.route.params.subscribe(params => {
          const id = params['id'];
          if (id) {
            this.userService.getUserById(id).subscribe((user: any) => {
              if (user) {
                this.user = user;

                console.log("this.currentUser.id - " + this.currentUser.id);
                console.log("this.user.id - " + this.user.id);
                this.currentUserPage = (this.currentUser.id == this.user.id);
                console.log("this.currentUserPage - " + this.currentUserPage);

                this.userService.getEventsByUserId(this.user.id)
                  .subscribe((events: any) => {
                    console.log("events - " + events);
                    this.userEvents = events;
                  });

                this.userService.getFriends(this.user.id)
                  .subscribe((friends: any) => {
                    console.log("friends - " + friends);
                    this.userFriends = friends;
                  });

              } else {
                console.log(`User with id '${id}' not found!`);
                this.gotoList();
              }
            });
          }
        });
      });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  goToEditUserPage(user: User): void {
    this.router.navigate(['users', user.id, 'edit']);
  }

  goToEditImagePage(user: User): void {
    this.router.navigate(['users', user.id, 'updateImage']);
  };

  goToEditPasswordPage(): void {
    this.router.navigate(['changePassword']);
  };

  gotoList() {
    this.router.navigate(['/users']);
  }

}
