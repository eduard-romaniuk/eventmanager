import {Component} from '@angular/core';
import {NoteService} from '../../../services/note.service';
import {ActivatedRoute, Router} from '@angular/router';
import {JQueryStatic} from 'jquery';

import {Note} from '../../../model/note'
import {Subscription} from "rxjs/Subscription";
import {FormGroup} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {ToastService} from "../../../services/toast.service";

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

  rootFolderId: number = 0;

  constructor(private auth: AuthService,
              private route: ActivatedRoute,
              private noteService: NoteService,
              private router: Router,
              private toastService: ToastService) {
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
    this.noteService.deleteNote(this.note.id).subscribe(any => {
      console.log('Folder id = ' + this.note.folder.id);
      this.toastService.success("The note was successfully deleted");
      if(this.note.folder.id == this.rootFolderId) {
        this.router.navigate(['folders/rootFolder']);
      } else {
        this.router.navigate(['folders/rootFolder/folder/' + this.note.folder.id]);
      }
    }, error => console.error(error));
  }

  convertToEvent(){

    this.router.navigate(['/note', this.note.id, 'convert']);
  }

  public isCreatorTest(): boolean {
    return this.userId === this.note.creator.id;
  }
}
