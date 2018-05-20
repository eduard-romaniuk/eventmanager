import {Component, OnInit} from '@angular/core';
import {EventService} from '../../services/event.service';
import {JQueryStatic} from 'jquery'

import {Event} from '../../model/event'
import {AuthService} from "../../services/auth.service";
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {dateLessThan, dateValidator, imageExtension} from "../../utils/validation-tools";
import {Router} from "@angular/router";
import {CloudinaryUploader} from "ng2-cloudinary";
import {ImageUploaderService} from "../../services/image-uploader.service";
import {User} from "../../model/user";
import {UserService} from "../../services/user.service";
import {Category} from "../../model/category";

@Component({
  selector: 'app-createEvent',
  templateUrl: './createEvent.component.html',
  styleUrls: ['./createEvent.component.css']
})
export class CreateEventComponent implements OnInit {

  event: Event = new Event();

  userFriends: User[];

  newParticipants: User[];

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  latitude: number;
  longitude: number;

  form: FormGroup;

  categories:Category[] =[];

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

  min = new Date();
  max = new Date(2049,11,31);

  constructor(private auth: AuthService,
              private eventService: EventService,
              private formBuilder: FormBuilder,
              private router: Router,
              private userService:UserService) {

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
      timeLineStartControl: ['', [Validators.required]],
      timeLineFinishControl: ['', [Validators.required]],
      periodControl: ['', [Validators.required, Validators.min(0)]],
    });
    this.setCurrentPosition();
    this.getCategories();
  }


  draft() {
    this.event.isSent = false;
    this.eventService.createEvent(this.event).subscribe (
      (id: number) => {
        this.router.navigate(['event/', id]);
      }
    );
  }

  create() {
    this.event.isSent = true;
    console.log(this.event.category.id);
    console.log(this.event);
    this.eventService.createEvent(this.event).subscribe (
      (id: number) => {
        this.addUsers(id);
        this.router.navigate(['event/', id]);
      }
    );

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

  addUsers(id){
    console.log(this.newParticipants);
    this.eventService.addUsers(this.newParticipants,id).subscribe();
  }

  getFriends() {
    console.log(this.event.creator);
    this.userService.getFriends(this.event.creator.id)
      .subscribe((friends: any) => {
        this.userFriends = friends;
      });
  }

  getCategories(){
    this.eventService.getCategories().subscribe((categories: any) => {
      this.categories = categories;
    });

  }

}
