import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {NoteService} from '../../../services/note.service'
import {Folder} from '../../../model/folder'
import {AuthService} from "../../../services/auth.service";
import {UserService} from "../../../services/user.service";
import {User} from "../../../model/user";
import {Router, ActivatedRoute} from "@angular/router";
import {Subject} from "rxjs/Subject";
import { Observable } from 'rxjs/Observable';


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
  queryToSearch = new Subject<string>();


  constructor(private route:ActivatedRoute,
              private auth:AuthService,
              private userService:UserService,
              private folderService:FolderService,
              private noteService:NoteService,
              private router:Router) {
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
    this.searchUser(this.queryToSearch).subscribe( (users : any) => {
      this.folder.users = users;
    });
  }

  delete() {
    this.folderService.delete(this.folder).subscribe(any => {
      this.router.navigate(['/folders/rootFolder']);
    });
  }

  updateMembers() {}

  searchUser(terms: Observable<string>) {
    return terms.distinctUntilChanged()
      .switchMap(term => this.userService.searchByLoginOrByNameAndSurname(term));
  }

}
