import { Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {User} from "../../../model/user";
import {AuthService} from "../../../services/auth.service";
import {UserService} from "../../../services/user.service";
import {Folder} from "../../../model/folder";


@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: ['./folder.component.css']
})
export class FolderComponent {

  folder: Folder = new Folder();
  currentUser: User = new User();


  constructor(private route: ActivatedRoute,
              private auth: AuthService,
              private userService: UserService) { }

  ngOnInit() {
    this.auth.current_user.subscribe(
      current_user => {
        this.currentUser = current_user;

        this.route.params.subscribe(params => {
          const folderId = params['id'];
          // if (id) {
          //   this.userService.getUserById(id).subscribe((user: any) => {
          //     if (user) {
          //       this.user = user;
          //
          //       console.log("this.currentUser.id - " + this.currentUser.id);
          //       console.log("this.user.id - " + this.user.id);
          //       this.currentUserPage = (this.currentUser.id == this.user.id);
          //       console.log("this.currentUserPage - " + this.currentUserPage);
          //
          //       this.userService.getEventsByUserId(this.user.id)
          //         .subscribe((events: any) => {
          //           console.log("events - " + events);
          //           this.userEvents = events;
          //         });
          //     } else {
          //       console.log(`User with id '${id}' not found!`);
          //       this.gotoList();
          //     }
          //   });
          // }
        });
      });
  }
  

}
