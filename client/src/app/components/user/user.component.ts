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
  incomeRequests: User[] = [];
  outcomeRequests: User[] = [];
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

                if(this.currentUserPage) {
                  this.userService.getCurrentUserEvents(this.user.id)
                    .subscribe((events: any) => {
                      console.log("events - " + events);
                      this.userEvents = events;
                    });
                    this.getIncomingRequests();
                } else {
                  this.userService.getEventsByUserId(this.user.id, false, true)
                    .subscribe((events: any) => {
                      console.log("events - " + events);
                      this.userEvents = events;
                    });
                }
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

  goToSettingsPage(): void {
    this.router.navigate(['users', this.currentUser.id, 'settings']);
  }

  gotoList() {
    this.router.navigate(['/users/all']);
  }

  goToUserWishList(user: User): void {
    this.router.navigate(['users', user.id, 'wishlist']);
  }

  getIncomingRequests() {
    this.userService.getIncomingRequests(this.user.id)
      .subscribe((incomeRequests: User[]) => {
        this.incomeRequests = incomeRequests;
      });
  }

  getOutcomingRequests() {
    this.userService.getOutcomingRequests(this.user.id)
      .subscribe((outcomeRequests: User[]) => {
        this.outcomeRequests = outcomeRequests;
      });
  }

  getFriends() {
    this.userService.getFriends(this.user.id)
      .subscribe((friends: any) => {
        this.userFriends = friends;
      });
  }

}
