import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'
import {User} from "../../model/user";
import {Observable} from "rxjs/Observable";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent {

  event : Event = new Event();

  constructor(private auth : AuthService,
              private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService) {

    auth.getUser().subscribe((data: any) => {this.event.creator = data});
    console.log(this.event.creator);
  }

  // ngOnInit() {
  //   this.route.params.subscribe(params => {
  //     console.log('On innit');
  //     console.log( params['user']);
  //   });
  // }

  create() {
    // TODO: Handle create error
    this.eventService.createEvent(this.event);
  }

}
