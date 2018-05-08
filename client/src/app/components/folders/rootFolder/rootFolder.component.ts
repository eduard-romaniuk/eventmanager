import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {Folder} from '../../../model/folder'
import {AuthService} from "../../../services/auth.service";
import {User} from "../../../model/user";


@Component({
  selector: 'app-folder',
  templateUrl: './rootFolder.component.html',
  styleUrls: ['./rootFolder.component.css']
})
export class RootFolderComponent {

  private folder: Folder = new Folder;
  folders: Folder[];
  creator: User = new User;

  constructor(private auth : AuthService,
              private folderService: FolderService) {
  }

  ngOnInit() {
    this.auth.current_user.subscribe((data: any) => {this.creator = data, console.log('data from auth.getUser:' + data)});
    console.log('Creator id - ' + this.creator.id);
    this.folderService.getFolders(3).subscribe(
      (folders: any) => {
        this.folders = folders;
      }
    );

  }

  create() {
    console.log('Creating folder: ' + this.folder.name);
    this.folderService.createFolder(this.folder);
  }

}
