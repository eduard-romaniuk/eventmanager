import {Component} from '@angular/core';
import {NoteService} from '../../../services/note.service';
import {ActivatedRoute} from '@angular/router';
import {JQueryStatic} from 'jquery';

import {Note} from '../../../model/note'
import {Subscription} from "rxjs/Subscription";
import {FormGroup} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-createEvent',
  templateUrl: './viewNote.component.html',
  styleUrls: ['./viewNote.component.css']
})
export class ViewNoteComponent {

  note: Note = new Note();
  userId: number;
  form: FormGroup;
  isCreator: boolean;

  sub: Subscription;

  public isLoading = true;

  constructor(private auth: AuthService,
              private route: ActivatedRoute,
              private noteService: NoteService) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;

      console.log("currentUserId = " + user.id);

      this.sub = this.route.params.subscribe(params => {
        const id = params['noteId'];
        console.log('Note id = ' + id);
        if (id) {
            this.noteService.getNoteById(id).subscribe((note: any) => {
              if (note) {
                this.note = note;
                console.log(`Note with id '${id}' was loaded!`);
                console.log(note);
                this.isCreator = this.isCreatorTest();
                console.log('Creator id = ' + this.note.creator.id);
              } else {
                console.log(`Note with id '${id}' not found!`);
              }
              this.isLoading = false;
            });
        }
      });
    });
  }

  public delete() {
    // this.noteService.deleteNote(this.note.id).subscribe(any => {
    //   this.router.navigate(['home']);
    // }, error => console.error(error));
  }

  convertToEvent(){
   
  }

  public isCreatorTest(): boolean {
    return this.userId === this.note.creator.id;
  }
}
