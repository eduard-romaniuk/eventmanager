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

  loadedEvents: Event[] = [];
  index = 0;
  events: Event[] = [];
  pattern: string = '';
  last_pattern: string = '';
  today: Date = new Date();
  date_range = [
        new Date(this.today.getFullYear(), this.today.getMonth(), 1, 0, 0),
        new Date(this.today.getFullYear(), this.today.getMonth(), 28, 23, 59)
  ];

  constructor(private router: Router, private eventService: EventService) { }

  ngOnInit() {
    this.eventService.getPublicEvents()
      .subscribe( (events : any) => {
        this.loadedEvents = events;
        this.loadMore(10);
      });
  }

  loadMore(step: number) {
    this.events = this.events.concat(this.loadedEvents.slice(this.index, this.index + step));
    this.index += step;
  }

  canLoadMore() {
    return this.index < this.loadedEvents.length;
  }

  search() {
    if(this.pattern !== this.last_pattern){
      this.last_pattern = this.pattern;
      console.log(this.pattern);

    }
  }

}
