import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {NoteService} from '../../../services/note.service'
import {Folder} from '../../../model/folder'
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../model/user";
import {Router, ActivatedRoute} from "@angular/router";
import {ToastService} from "../../../services/toast.service";
import {Note} from "../../../model/note";



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
  rootFolderNotes: Note[];


  constructor(private route:ActivatedRoute,
              private auth:AuthService,
              private folderService:FolderService,
              private noteService:NoteService,
              private router:Router,
              private toastService: ToastService) {
  }

  ngOnInit() {
    this.auth.current_user.subscribe((currentUser:any) => {
      this.currentUser = currentUser;
      this.route.params.subscribe(params => {
          this.folder.id = params['id'];
          if (this.folder.id != this.rootFolderId) {
            this.folderService.getFolderWithCheck(this.folder.id).subscribe((folder:Folder) => {
              if (folder) {
                this.folder = folder;
                console.log('loaded folder id - ' + this.folder.id);
                console.log('Creator.Id of loaded folder - ' + this.folder.creator.id);
                if (this.currentUser.id == this.folder.creator.id) {
                  this.isCreator = true;
                }
                  this.noteService.getFolderNotes(this.folder.id).subscribe(
                    (notes:any) => {
                      this.folder.notes = notes;
                      console.log('loaded notes: ' + this.folder.notes);
                    }
                  );
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
      this.toastService.success("The folder was successfully deleted");
    });
  }

  getAllMembers() {
    this.folderService.getAllMembers(this.folder.id)
      .subscribe((members: any) => {
        this.folder.members = members;
        console.log('Members: ' + this.folder.members);
      });
  }

  updateMembers() {
    this.folderService.updateMembers(this.folder).subscribe((code: number) => {
      console.log(code);
      if(code == 0) {
        this.toastService.success("Members list has been successfully updated");
      }
    });
  }

  loadRootNotes() {
    this.noteService.getFolderNotes(this.rootFolderId).subscribe(
      (notes: any) => {
        this.rootFolderNotes = notes;

        console.log('loaded notes: ' + this.rootFolderNotes);
      }
    );
  }

  moveNotes() {
    for(let note of this.rootFolderNotes) {
      if(note.notFromRootFolder == true) {
        note.folder = new Folder();
        note.folder.id = this.folder.id;
        console.log('new folder id of note: ' + note.folder.id);
        this.folder.notes.push(note);
      }
    }
    this.noteService.moveNotes(this.rootFolderNotes).subscribe((data: any) => {
      this.toastService.success("Notes was successfully mowed");
    });
  }
}
