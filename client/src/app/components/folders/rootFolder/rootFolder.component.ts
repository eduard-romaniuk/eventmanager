import {Component} from '@angular/core';
import {FolderService} from '../../../services/folder.service'
import {Folder} from '../../../model/folder'


@Component({
  selector: 'app-folder',
  templateUrl: './rootFolder.component.html',
  styleUrls: ['./rootFolder.component.css']
})
export class RootFolderComponent {

  private folder: Folder = new Folder;

  constructor(private folderService: FolderService) {
  }

  create() {
    console.log('Creating folder: ' + this.folder.name);
    this.folderService.createFolder(this.folder);
  }

}
