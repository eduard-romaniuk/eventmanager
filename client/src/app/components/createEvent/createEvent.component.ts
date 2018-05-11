import {Component, OnInit} from '@angular/core';
import {EventService} from '../../services/event.service';
import {JQueryStatic} from 'jquery'

import {Event} from '../../model/event'
import {AuthService} from "../../services/auth.service";
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {dateLessThan, dateValidator} from "../../utils/validation-tools";
import {Router} from "@angular/router";
import {CloudinaryUploader} from "ng2-cloudinary";
import {ImageUploaderService} from "../../services/image-uploader.service";

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent implements OnInit {

  event: Event = new Event();

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  latitude: number;
  longitude: number;

  form: FormGroup;

  constructor(private auth: AuthService,
              private eventService: EventService,
              private formBuilder: FormBuilder,
              private router: Router) {

    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      let res: any = JSON.parse(response);
      this.event.image = res.url;
      console.log(`res - ` + JSON.stringify(res) );
      return { item, response, status, headers };
    };
  }

  ngOnInit() {
    this.auth.getUser().subscribe((data: any) => {
      this.event.creator = data
    });
    console.log(this.event.creator);
    this.form = this.formBuilder.group({
      eventNameControl: ['', [Validators.required]],
      descriptionControl: ['', [Validators.required]],
      timeLineStartControl: ['', [Validators.required, dateValidator()]],
      timeLineFinishControl: ['', [Validators.required]],
      periodControl: ['', [Validators.required, Validators.min(0)]],
    }, {validator: dateLessThan('timeLineStartControl', 'timeLineFinishControl'),});
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

  onChoseLocation(event) {
    this.latitude = event.coords.lat;
    this.longitude = event.coords.lng;
    this.event.place = this.latitude + "/" + this.longitude;
    console.log(this.event.place)
  }

  upload() {
    this.uploader.uploadAll();
  }

}
