import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../../model/user';
import { UserService } from '../../services/user.service';
import {AuthService} from "../../services/auth.service";
import {isUndefined} from "util";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  currentUser: User = new User();
  user: User = new User();
  relationshipStatus: String;
  currentUserIsActionUser: boolean;

  userEvents: Event[];
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

                if(!this.currentUserPage){
                  this.userService.getRelationshipStatusAndActionUserId(this.currentUser.id, this.user.id)
                    .subscribe((data: Map<string, any>) => {
                      const statusName = data['status_name'];

                      //TODO Fix
                      if(isUndefined(statusName)){
                        this.relationshipStatus = '';
                        this.currentUserIsActionUser = false;
                      } else {
                        this.relationshipStatus = data['status_name'];
                        this.currentUserIsActionUser = (data['action_user_id'] == this.currentUser.id);
                        console.log("this.relationshipStatus - \'" + this.relationshipStatus + "'");
                        console.log("data['action_user_id'] - " + data['action_user_id']);
                      }
                    });
                }

                this.userService.getEventsByUserId(this.user.id)
                  .subscribe((events: any) => {
                    console.log("events - " + events);
                    this.userEvents = events;
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

  addToFriends(userId: number) {
    this.userService.addFriendRequest(this.currentUser.id, userId).subscribe(() => {
      this.updateRelationshipStatus(this.currentUser.id, this.user.id);
      this.currentUserIsActionUser = true;
    }, error => console.error(error));
  }

  deleteRequest(userId: number) {
    this.userService.deleteRelationship(this.currentUser.id, userId).subscribe(() => {
      this.updateRelationshipStatus(this.currentUser.id, userId);
    }, error => console.error(error));
  }

  acceptRequest(userId: number) {
    this.userService.acceptFriendRequest(userId, this.currentUser.id).subscribe(() => {
      this.updateRelationshipStatus(userId, this.currentUser.id);
      this.currentUserIsActionUser = true;
    }, error => console.error(error));
  }

  declineRequest(userId: number) {
    this.userService.declineFriendRequest(userId, this.currentUser.id).subscribe(() => {
      this.updateRelationshipStatus(userId, this.currentUser.id);
      this.currentUserIsActionUser = true;
    }, error => console.error(error));
  }

  deleteFromFriends(userId: number) {
    this.deleteRequest(userId);
  }

  private updateRelationshipStatus(userOneId: number, userTwoId: number){
    this.userService.getRelationshipStatus(userOneId, userTwoId)
      .subscribe((relationshipStatus : String) => {
        console.log("New relationship status - " + relationshipStatus);
        this.relationshipStatus = relationshipStatus;
      });
  }

}
