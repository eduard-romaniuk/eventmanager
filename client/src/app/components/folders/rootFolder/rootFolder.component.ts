import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {NoteService} from '../../../services/note.service'
import {Folder} from '../../../model/folder'
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../model/user";
import { FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Note} from "../../../model/note";


@Component({
  selector: 'app-folder',
  templateUrl: './rootFolder.component.html',
  styleUrls: ['./rootFolder.component.css']
})
export class RootFolderComponent {

  folder: Folder = new Folder;
  folders: Folder[];
  notes: Note[];
  currentUser: User;
  form: FormGroup;
  rootFolderId:number = 0;

  constructor(private auth : AuthService,
              private folderService: FolderService,
              private noteService: NoteService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.auth.current_user.subscribe((currentUser: any) => {
      this.currentUser = currentUser;
      this.folderService.getFolders(this.currentUser.id).subscribe(
        (folders: any) => {
          this.folders = folders;
        }
      );
    });
    this.form = this.formBuilder.group({
      folderNameControl: ['', [ Validators.required]]
    });
    this.noteService.getFolderNotes(this.rootFolderId).subscribe(
      (notes: any) => {
        this.notes = notes;
        console.log('loaded notes: ' + this.notes);
      }
    );
  }

  createFolder() {
    console.log('Creating folder: ' + this.folder.name);
    this.folder.creator = this.currentUser;
    this.folderService.createFolder(this.folder).subscribe((folder: Folder) => {
      this.folders.push(folder);
    });
  }

}
