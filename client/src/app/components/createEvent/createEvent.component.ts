import {Component} from '@angular/core';
import {EventService} from '../../services/event.service';
import {JQueryStatic} from 'jquery';

import { Event } from '../../model/event'
import {AuthService} from "../../services/auth.service";
import { FormGroup, FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent {

  private event : Event = new Event();

  form: FormGroup;

  constructor(private auth : AuthService,
              private eventService: EventService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((data: any) => {this.event.creator = data});
    console.log(this.event.creator);
    this.form = this.formBuilder.group({
      eventNameControl: ['', [ Validators.required]],
      descriptionControl: ['', [ Validators.required]],
      timeLineFinishControl: ['', [ Validators.min(this.event.timeLineStart)]],
      periodControl: ['', [ Validators.required, Validators.min(0)]],
    });
  }

  create() {
    // TODO: Handle create error
    this.event.isSent = false;
    this.eventService.createEvent(this.event);
  }

  publish() {
    // TODO: Handle create error
    this.event.isSent = true;
    this.eventService.createEvent(this.event);
  }

}
