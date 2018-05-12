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

  public  createFolder(folder) {
    return this.http.post(this.base_url, folder);
  }

  public getFolders(userId: number) {
    return this.http.get(this.base_url + userId + '/all');
  }

  public getFolderWithCheck(folderId: number) {
    return this.http.get(this.base_url + folderId + '/checkUser');
  }

}
