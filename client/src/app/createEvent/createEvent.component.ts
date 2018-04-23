import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { JQueryStatic } from 'jquery';

declare var $:JQueryStatic;

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent {

  eventData = {eventName: '', description: '', place: '', timeLineStart: '', timeLineFinish: '', period: '', isPrivate: false};

  constructor(private evt: EventService, private http: HttpClient, private router: Router) {
  }

  create() {
    // TODO: Handle create error
    this.evt.create(this.eventData, () => {
      $('#CreateEventModal').modal('hide');
      this.router.navigateByUrl('/home');
    });
  }

}
