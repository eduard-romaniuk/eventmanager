import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'
import {User} from "../../model/user";
import {Observable} from "rxjs/Observable";
import {AuthService} from "../../services/auth.service";
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import {dateValidator} from "../../utils/validation-tools";

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent implements OnInit{

  event : Event = new Event();

  latitude: number;
  longitude: number;

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
      timeLineStartControl:['', [ Validators.required,dateValidator(new Date())]],
      timeLineFinishControl: ['', [ Validators.required,dateValidator(new Date(this.event.timeLineStart))]],
      periodControl: ['', [ Validators.required, Validators.min(0)]],
    });
    this.setCurrentPosition();
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
  private setCurrentPosition() {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
      });
    }
  }

  onChoseLocation(event){
    console.log(event);
    this.latitude=event.coords.lat;
    this.longitude=event.coords.lng;

  }


}
