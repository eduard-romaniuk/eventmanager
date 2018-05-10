import {Component} from '@angular/core';
import {EventService} from '../../services/event.service';
import {Router, ActivatedRoute} from '@angular/router';
import {JQueryStatic} from 'jquery';

import {Event} from '../../model/event'
import {User} from "../../model/user";
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs/Subscription";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-createEvent',
  templateUrl: './viewEvent.component.html',
  styleUrls: ['./viewEvent.component.css']
})
export class ViewEventComponent {

  event: Event = new Event();
  userId: number;
  priority: String;
  form: FormGroup;
  isParticipant: boolean;
  isCreator: boolean;
  participationStr: String;
  participants: User[];

  latitude: Number;
  longitude: Number;


  sub: Subscription;

  constructor(private auth: AuthService,
              private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;
      console.log("currentUserId = " + user.id);
    });
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.eventService.isParticipantRequest(id).subscribe((participation: String) => {
          if (participation) {
            this.participationStr = participation;
            this.isParticipant = (this.participationStr == "true");
          } else {
            console.log(`Participation not found!`);
          }
        });
        this.eventService.getEventById(id).subscribe((event: any) => {
          if (event) {
            this.event = event;
            this.isCreator = this.isCreatorTest();
            this.Position();
            console.log(`Event with id '${id}' was loaded!`);
            console.log(event);
          } else {
            console.log(`Event with id '${id}' not found!`);
          }
        });

        this.eventService.getPriority(id).subscribe((priority: String) => {
          if (priority) {
            this.priority = priority;
          } else {
            console.log(`Priority not found!`);
          }
        });

      }
    });
    console.log("loadedEventCreator = " + this.event.creator)
  }

  private Position() {

    let coords = this.event.place.split("/");
    this.latitude = Number(coords[0]);
    this.longitude = Number(coords[1]);
    console.log(this.latitude);
    console.log(this.longitude);

  }

  public submitPriority() {
    this.eventService.changePriority(this.event.id, this.priority);
  }

  public join() {
    this.eventService.joinToEvent(this.event.id).subscribe();
    window.location.reload();
  }

  public leave() {
    this.eventService.leaveEvent(this.event.id).subscribe();
    window.location.reload();
  }

  public delete() {

  }

  invite(){

  }
  public isCreatorTest(): boolean {
    return this.userId === this.event.creator.id;
  }

  public showParticipants() {
    this.eventService.getParticipants(this.event.id)
      .subscribe((users: any) => {
        this.participants = users;
      });
    console.log(this.participants)
  }

  goToChatWithCreator() {
    this.router.navigate(['event', this.event.id, 'chats', 'withCreator']);
  }

  goToChatWithoutCreator() {
    this.router.navigate(['event', this.event.id, 'chats', 'withoutCreator']);
  }

  low() {
    if (this.priority == "0") return true
  }

  normal() {
    if (this.priority == "1") return true
  }

  urgent() {
    if (this.priority == "2") return true
  }
}
