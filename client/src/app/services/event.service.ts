import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Event } from '../model/event';
import { User } from '../model/user';
//import { AuthService } from 'auth.service';
import {Observable} from "rxjs/Observable";
import { ActivatedRoute, Router } from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class EventService {

  currentUserId: number;
  //authService :AuthService;
  private base_url = '/event/';

  constructor(private http: HttpClient, private route: ActivatedRoute) {
    //this.currentUser = currentUserIn;
    //this.route.params.subscribe(params => )

  }

  // ngOnInit() {
  //   this.route.params.subscribe(params => {
  //     console.log('On innit');
  //     console.log( params['userId']);
  //   });
  // }

  public createEvent(event) {
    //this.currentUser = this.authService.getUser();
    console.log('Create event');
    console.log(event);
    console.log(this.currentUserId);
    return this.http.post(this.base_url, event).subscribe(
           (data:any) => {
             console.log(data);
           }
         );
  }
}
