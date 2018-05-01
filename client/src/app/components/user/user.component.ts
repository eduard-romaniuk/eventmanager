import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../../model/user';
import { UserService } from '../../services/user.service';
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User = new User();
  userEvents: Event[];
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
            this.userService.getEventsByUserId(this.user.id)
              .subscribe( (events : any) => {
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
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  gotoList() {
    this.router.navigate(['/users']);
  }

}
