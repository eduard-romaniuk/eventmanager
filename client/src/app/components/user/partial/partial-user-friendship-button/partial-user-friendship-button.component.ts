import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/user";
import {UserService} from "../../../../services/user.service";
import {isUndefined} from "util";

@Component({
  selector: 'app-partial-user-friendship-button',
  templateUrl: './partial-user-friendship-button.component.html',
  styleUrls: ['./partial-user-friendship-button.component.css']
})
export class PartialUserFriendshipButtonComponent implements OnInit {

  @Input() currentUser: User = new User();
  @Input() user: User = new User();

  relationshipStatus: String;
  currentUserIsActionUser: boolean;

  isCurrentUser: boolean = false;

  constructor(private userService: UserService) { }

  ngOnInit() {
    console.log("this.currentUser.id - \'" + this.currentUser.id + "'");

    if(this.currentUser.id==this.user.id){
      this.isCurrentUser = true;
    } else {
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
          }

          console.log("this.relationshipStatus - \'" + this.relationshipStatus + "'");
          console.log("this.currentUserIsActionUser - " + this.currentUserIsActionUser);
        });
    }

  }

  addToFriends(userId: number) {
    this.userService.addFriendRequest(this.currentUser.id, userId).subscribe(() => {
      this.updateRelationshipStatus(this.currentUser.id, userId);
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
