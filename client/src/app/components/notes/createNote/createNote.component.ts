import {Component} from '@angular/core';
import {EventService} from '../../../services/event.service';
import {JQueryStatic} from 'jquery';
import { ActivatedRoute} from '@angular/router';

import { Event } from '../../../model/event'
import {AuthService} from "../../../services/auth.service";
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import {Folder} from "../../../model/folder";

@Component({
  selector: 'app-createEvent',
  templateUrl: './createNote.component.html',
  styleUrls: ['./createNote.component.css']
})
export class CreateNoteComponent {

  private note : Event = new Event();

  form: FormGroup;
  folder: Folder;

  constructor(private route: ActivatedRoute,
              private auth : AuthService,
              private eventService: EventService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((data: any) => {this.note.creator = data});
    console.log(this.note.creator);
    this.route.params.subscribe(params => {
      const folderId = params['folderId']
      console.log('folder id - ' + folderId);
      if (folderId) {

        this.note.folder = new Folder();
        this.note.folder.id = folderId;
      }
    })
    this.form = this.formBuilder.group({
      eventNameControl: ['', [ Validators.required]],
      descriptionControl: ['', [ Validators.required]],
      periodControl: ['', [ Validators.required, Validators.min(0)]],
    });
  }

  create() {
    // TODO: Handle create error
    this.eventService.createEvent(this.note);
  }

}
