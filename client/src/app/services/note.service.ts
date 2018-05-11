/**
 * Created by Shvets on 04.05.2018.
 */

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable()
export class NoteService {

  private base_url = '/note/';


  constructor(private http: HttpClient, private router: Router) {

  }

}
