import {Component} from '@angular/core';
import {NoteService} from '../../../services/note.service';
import {Router, ActivatedRoute} from '@angular/router';
import {JQueryStatic} from 'jquery';

import {Note} from '../../../model/note'
import {User} from "../../../model/user";
import {Subscription} from "rxjs/Subscription";
import {FormGroup} from "@angular/forms";
import {ToastService} from "../../../services/toast.service";
import {UserService} from "../../../services/user.service";
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
              private router: Router,
              private noteService: NoteService,
              private toast: ToastService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;

      console.log("currentUserId = " + user.id);

      this.sub = this.route.params.subscribe(params => {
        const id = params['id'];
        if (id) {

            this.noteService.getNoteById(id).subscribe((note: any) => {
              if (note) {
                this.note = note;
                this.isCreator = this.isCreatorTest();
                console.log(`Note with id '${id}' was loaded!`);
                console.log(note);
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
    // this.event.isSent=true;
    // this.eventService.updateEvent(this.event).subscribe((user: any) => {
    //   this.router.navigate(['event/', this.event.id]);
    // }, error => console.error(error));
  }

  public isCreatorTest(): boolean {
    return this.userId === this.note.creator.id;
  }
}
