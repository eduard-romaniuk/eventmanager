import { Component, OnInit } from '@angular/core';
import {Subscription} from "rxjs/Subscription";
import {ImageUploaderService} from "../../services/image-uploader.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CloudinaryUploader} from "ng2-cloudinary";

import {Event} from "../../model/event";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-event-edit-image',
  templateUrl: './event-edit-image.component.html',
  styleUrls: ['./event-edit-image.component.css']
})
export class EventEditImageComponent implements OnInit {

  event: Event = new Event();

  sub: Subscription;

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService) {
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

  upload() {
    this.uploader.uploadAll();
  }

  uploadImage() {
    console.log("this.event - " + this.event);
    this.eventService.updateEvent(this.event).subscribe((user: any) => {
      this.goToEdit();
    }, error => console.error(error));
  }

  goToEdit(): void {
    this.router.navigate(['event', this.event.id,'edit']);
  };

}
