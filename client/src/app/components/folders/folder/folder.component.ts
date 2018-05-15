import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {NoteService} from '../../../services/note.service'
import {Folder} from '../../../model/folder'
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../model/user";
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Note} from "../../../model/note";
import {Router, ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: ['./folder.component.css']
})
export class FolderComponent {

  private folder:Folder = new Folder;
  isCreator:boolean = false;
  currentUser:User;
  rootFolderId:number = 0;
  access:boolean = true;


  constructor(private route:ActivatedRoute,
              private auth:AuthService,
              private folderService:FolderService,
              private noteService:NoteService,
              private formBuilder:FormBuilder,
              private router:Router) {
  }

  ngOnInit() {
    this.auth.current_user.subscribe((currentUser:any) => {
      this.currentUser = currentUser;
      this.route.params.subscribe(params => {
          this.folder.id = params['id'];
          if (this.folder.id != this.rootFolderId) {
            this.folderService.getFolderWithCheck(this.folder.id).subscribe((folder:Folder) => {
              console.log('folder creator id = ' + folder.creator.id);
              if (folder) {
                this.folder = folder;
                console.log('loaded folder id - ' + this.folder.id);
                console.log('Creator.Id of loaded folder - ' + this.folder.creator.id);
                if (this.currentUser.id == this.folder.creator.id) {
                  this.isCreator = true;
                  this.noteService.getFolderNotes(this.folder.id).subscribe(
                    (notes:any) => {
                      this.folder.notes = notes;
                      console.log('loaded notes: ' + this.folder.notes);
                    }
                  );
                }
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
    });
  }

  delete() {
    this.folderService.delete(this.folder).subscribe(any => {
      this.router.navigate(['/folders/rootFolder']);
    });
  }

}
