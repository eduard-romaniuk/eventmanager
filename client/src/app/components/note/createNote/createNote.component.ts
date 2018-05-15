import {Component, OnInit} from '@angular/core';
import {JQueryStatic} from 'jquery'

import {AuthService} from "../../../services/auth.service";
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router, ActivatedRoute} from "@angular/router";
import {CloudinaryUploader} from "ng2-cloudinary";
import {ImageUploaderService} from "../../../services/image-uploader.service";
import {NoteService} from "../../../services/note.service";
import {Note} from "../../../model/note";
import {FolderService} from "../../../services/folder.service";
import {Folder} from "../../../model/folder";

@Component({
  selector: 'app-createNote',
  templateUrl: './createNote.component.html',
  styleUrls: ['./createNote.component.css']
})
export class CreateNoteComponent implements OnInit {

  note:Note = new Note();

  access:boolean;

  uploader:CloudinaryUploader = ImageUploaderService.getUploader();

  latitude:number;
  longitude:number;

  form:FormGroup;

  folderId:number = 0;
  rootFolderId:number = 0;

  constructor(private route:ActivatedRoute,
              private auth:AuthService,
              private formBuilder:FormBuilder,
              private folderService:FolderService,
              private noteService:NoteService,
              private router:Router) {

    this.uploader.onSuccessItem = (item:any, response:string, status:number, headers:any):any => {
      let res:any = JSON.parse(response);
      this.note.image = res.url;
      console.log(`res - ` + JSON.stringify(res));
      return {item, response, status, headers};
    };
  }

  ngOnInit() {
    this.access = true;
    this.auth.getUser().subscribe((data:any) => {
      this.note.creator = data;
      console.log('creator - ' + this.note.creator);
    });
    this.route.params.subscribe(params => {
        const folderId = params['folderId'];
        if (folderId != this.folderId) {
          this.folderId = folderId;
          this.folderService.getFolderWithCheck(folderId).subscribe((folder:Folder) => {
            if (folder) {
              this.note.folder = folder;
              console.log('loaded folder id - ' + this.note.folder.id);
            } else {
              console.log('The folder does not exist or the user does not have permission');
              this.access = false;
            }
          })
        } else {
          console.log('root folder');
        }
      }
    );
    this.form = this.formBuilder.group({
      eventNameControl: ['', [Validators.required]],
      descriptionControl: ['', [Validators.required]],
      periodControl: ['', [Validators.required, Validators.min(0)]],
    });
    this.setCurrentPosition();
  }


  create() {
    this.note.isSent = false;
    console.log('Creating note: ' + this.note);
    this.noteService.createNote(this.note).subscribe(
      (note:Note) => {
        console.log('created note: ' + note);
        if(this.folderId == this.rootFolderId) {
          this.router.navigate(['/folders/rootFolder']);
        }
        else {
          this.router.navigate(['/folders/rootFolder/folder/', this.folderId]);
        }
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

  onChoseLocation(note) {
    this.latitude = note.coords.lat;
    this.longitude = note.coords.lng;
    this.note.place = this.latitude + "/" + this.longitude;
    console.log(this.note.place)
  }

  upload() {
    this.uploader.uploadAll();
  }
}
