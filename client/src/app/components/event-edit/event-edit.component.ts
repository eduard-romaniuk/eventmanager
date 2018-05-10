import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { Event } from '../../model/event';
import { EventService } from '../../services/event.service';
import {User} from "../../model/user";

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrls: ['./event-edit.component.css']
})
export class EventEditComponent implements OnInit, OnDestroy {

  event: Event = new Event();

  sub: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.eventService.getEvent(id).subscribe((event: any) => {
          if (event) {
            this.event = event;
          } else {
            console.log(`Event with id '${id}' not found!`);
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  publish() {
    this.event.isSent = true;
    this.save();
  }

  save() {
    this.eventService.updateEvent(this.event).subscribe(response => {
      this.router.navigate(['home']);
    }, error => console.error(error));
  }
  goToEditImagePage(event: Event): void {
    this.router.navigate(['event', event.id, 'updateImage']);
  };
}
