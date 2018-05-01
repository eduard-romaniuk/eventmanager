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

  private base_url = '/event/';
  private response: Response;


  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {

  }

  getEventById(id) {
    return this.http.get(this.base_url + id);
  }

  public createEvent(event) {
    //this.currentUser = this.authService.getUser();
    console.log('Create event');
    console.log(event);

    this.http.post(this.base_url, event).subscribe(
      (id: number) => {
             console.log("Id: " + id)
             this.router.navigate(['event/', id]);
           }
         );

  }

  public getEvent(id: number): Observable<Event> {
     return this.http.get<Event>(this.base_url + id);
  }

  public updateEvent(event: Event): Observable<Object> {
    return this.http.post(this.base_url + event.id, event);
  }

  public getPublicEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.base_url);
  }
}
