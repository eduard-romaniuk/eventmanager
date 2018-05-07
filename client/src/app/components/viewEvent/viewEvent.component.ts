import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'
import {User} from "../../model/user";
import {Observable} from "rxjs/Observable";
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
  priority:String;
  form: FormGroup;
  priority_id:number;
  isParticipant:boolean;
  participationStr:String;
  participants: User[];

  sub: Subscription;

  constructor(private auth : AuthService,
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
        this.eventService.isParticipantRequest(id).subscribe((participation:String) => {
          if (participation) {
            this.participationStr = participation;
            this.isParticipant =(this.participationStr=="true");
          } else {
            console.log(`Participation not found!`);
          }
        });
        this.eventService.getEventById(id).subscribe((event: any) => {
          if (event) {
            this.event = event;
            console.log(`Event with id '${id}' was loaded!`);
            console.log(event);
          } else {
            console.log(`Event with id '${id}' not found!`);
          }
        });
       this.eventService.getPriority(id).subscribe((priority:String) => {
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

  public submitPriority(){
    this.eventService.changePriority(this.event.id,this.priority_id).subscribe();
    this.eventService.getPriority(this.event.id).subscribe((priority:String) => {
      if (priority) {
        this.priority = priority;
        console.log(priority);
      } else {
        console.log(`Priority not found!`);
      }
    });
    console.log("Priority change to "+this.priority_id);
  }
  public join(){
    this.eventService.joinToEvent(this.event.id).subscribe();
    window.location.reload();
  }
  public isCreator(): boolean {
    return this.userId === this.event.creator.id;
  }

  public showParticipants() {
    this.eventService.getParticipants(this.event.id)
      .subscribe( (users : any) => {
        this.participants = users;
      });
    console.log(this.participants)
  }
  goToChat() {
      this.router.navigate(['/chats']);
    }
}
