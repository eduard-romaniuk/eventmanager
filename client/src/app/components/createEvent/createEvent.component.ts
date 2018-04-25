import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent {

  event : Event = new Event();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService) { }

  create() {
    // TODO: Handle create error
    this.eventService.createEvent(this.event);
  }

}
