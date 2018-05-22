import {Component, OnInit, OnDestroy, ViewChild, ElementRef, NgZone} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CloudinaryUploader} from "ng2-cloudinary";
import {ImageUploaderService} from "../../../services/image-uploader.service";
import {AuthService} from "../../../services/auth.service";
import {imageExtension} from "../../../utils/validation-tools";
import {Note} from "../../../model/note";
import {NoteService} from "../../../services/note.service";

@Component({
  selector: 'app-note-edit',
  templateUrl: './note-edit.component.html',
  styleUrls: ['./note-edit.component.css']
})
export class NoteEditComponent implements OnInit, OnDestroy {

  note: Note = new Note();

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  form: FormGroup;

  sub: Subscription;

  editorConfig = {
    editable: true,
    spellcheck: false,
    height: '10rem',
    minHeight: '5rem',
    placeholder: 'Note description...',
    translate: 'no',
    "toolbar": [
      ["bold", "italic", "underline", "strikeThrough"],
      ["fontSize", "color"],
      ["justifyLeft", "justifyCenter", "justifyRight", "justifyFull"],
      ["undo", "redo"],
      ["horizontalLine", "orderedList", "unorderedList"],
    ]
  };

  imageUploading = false;

  constructor(private auth: AuthService,
              private noteService: NoteService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router) {

    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      this.imageUploading = false;
      let res: any = JSON.parse(response);
      this.note.image = res.url;
      console.log(`res - ` + JSON.stringify(res) );
      return { item, response, status, headers };
    };
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['noteId'];
      if (id) {
        this.noteService.getNoteByIdForUpdate(id).subscribe((note: any) => {
          if (note) {
            this.note = note;
          } else {
            console.log(`Note with id '${id}' not found!`);
          }
        });
      }
    });
    this.form = this.formBuilder.group({
      noteNameControl: ['', [Validators.required]],
      descriptionControl: ['', [Validators.required]],
        image: ['', ]},
      {validator: imageExtension('image')});

  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  upload() {
    if (this.form.get("image").valid) {
      this.imageUploading = true;
      this.uploader.uploadAll();
    }
  }

  save() {
    console.log(this.note);
    this.noteService.updateNote(this.note).subscribe((data: any) => {
      this.router.navigate(['note', this.note.id]);
    }, error => console.error(error));
  }


}
