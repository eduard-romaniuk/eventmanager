/**
 * Created by Shvets on 04.05.2018.
 */


import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Folder} from '../model/folder'

@Injectable()
export class FolderService {

  private base_url = '/folder/';


  constructor(private http: HttpClient, private router: Router) {

  }

  public createFolder(folder) {
    this.http.post(this.base_url, folder).subscribe(
      (id: number) => {
        this.router.navigate(['note/', id]);
      }
    );
  }

  public getFolders(creatorId: number) {
    console.log('creator id = ' + creatorId);
    return this.http.get(this.base_url + creatorId + '/all');
  }

}
