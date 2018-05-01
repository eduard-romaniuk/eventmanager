import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../../model/user';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.css']
})
export class EventListComponent implements OnInit {

  events: Event[];

  constructor(private router: Router, private eventService: EventService) { }

  ngOnInit() {
    this.eventService.getPublicEvents()
      .subscribe( (events : any) => {
        this.events = events;
        console.log(events);
      });
  }

}
