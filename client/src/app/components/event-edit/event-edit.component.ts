import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { Event } from '../../model/event';
import { EventService } from '../../services/event.service';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CloudinaryUploader} from "ng2-cloudinary";
import {ImageUploaderService} from "../../services/image-uploader.service";
import {AuthService} from "../../services/auth.service";
import {Category} from "../../model/category";

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrls: ['./event-edit.component.css']
})
export class EventEditComponent implements OnInit, OnDestroy {

  event: Event = new Event();

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  latitude: Number;
  longitude: Number;

  form: FormGroup;

  sub: Subscription;

  categories:Category[] =[];

  min = new Date();
  max = new Date(2049,11,31);


  editorConfig = {
    editable: true,
    spellcheck: false,
    height: '10rem',
    minHeight: '5rem',
    placeholder: 'Event description...',
    translate: 'no',
    "toolbar": [
      ["bold", "italic", "underline", "strikeThrough"],
      ["fontSize", "color"],
      ["justifyLeft", "justifyCenter", "justifyRight", "justifyFull"],
      ["undo", "redo"],
      ["horizontalLine", "orderedList", "unorderedList"],
    ]
  };

  constructor(private auth: AuthService,
              private eventService: EventService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router) {

    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      let res: any = JSON.parse(response);
      this.event.image = res.url;
      console.log(`res - ` + JSON.stringify(res) );
      return { item, response, status, headers };
    };
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.eventService.getEvent(id).subscribe((event: any) => {
          if (event) {
            this.event = event;
            this.Position();
          } else {
            console.log(`Event with id '${id}' not found!`);
          }
        });
      }
    });
    this.form = this.formBuilder.group({
      eventNameControl: ['', [Validators.required]],
      descriptionControl: ['', [Validators.required]],
      timeLineStartControl: ['', [Validators.required]],
      timeLineFinishControl: ['', [Validators.required]],
      periodControl: ['', [Validators.required]],
    });
    this.getCategories();

  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private Position() {

    let coords = this.event.place.split("/");
        this.latitude =Number(coords[0]);
        this.longitude =Number(coords[1]);
        console.log(this.latitude);
        console.log(this.longitude);

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
  publish() {
    this.event.isSent = true;
    console.log(this.event);
    this.eventService.updateEvent(this.event).subscribe((user: any) => {
      this.router.navigate(['event/', this.event.id]);
    }, error => console.error(error));
  }

  save() {
    this.event.isSent = false;
    console.log(this.event);
    this.eventService.updateEvent(this.event).subscribe((user: any) => {
      this.router.navigate(['event', this.event.id]);
    }, error => console.error(error));
  }

  getCategories(){
    this.eventService.getCategories().subscribe((categories: any) => {
      this.categories = categories;
    });

  }

}
