import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Event } from '../model/event.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class EventService {

  headers: HttpHeaders;

  private eventUrl = 'http://localhost:80/event';

  constructor(private http: HttpClient) {
  }

  public createEvent(event) {
    console.log('Create event');
    console.log(event);
    return this.http.post(this.eventUrl, event).subscribe(
           (data:any) => {
             console.log(data);
           }
         );
  }

  // create(eventData, callback) {
  //
  //   this.headers = new HttpHeaders({
  //     "Content-Type" : "application/json"
  //   });
  //
  //   console.log(`CreateNewEvent(${this.headers.get("Content-Type")})`);
  //
  //   this.http.post(`http://localhost:80/event`,
  //     JSON.stringify({
  //       name: eventData.eventName,
  //       description: eventData.description,
  //       place: eventData.place,
  //       timeLineStart: eventData.timeLineStart,
  //       timeLineFinish: eventData.timeLineFinish,
  //       period: eventData.period,
  //       isSent: false,
  //       isPrivate: eventData.isPrivate
  //     }), {headers: this.headers})
  //     .subscribe(
  //       (data:any) => {
  //         console.log(data);
  //       }
  //     )
  //
  //   // this.http.post('http://localhost:80/event', {body : {
  //   //   "name": eventData.eventName,
  //   //   "description": eventData.description,
  //   //   "place": eventData.place,
  //   //   "timeLineStart": eventData.timeLineStart,
  //   //   "timeLineFinish": eventData.timeLineFinish,
  //   //   "period": eventData.period,
  //   //   "isSent":false,
  //   //   "isPrivate": eventData.isPrivate
  //   // }}, {headers: this.headers}).subscribe(
  //   //   (data:any) => {
  //   //     console.log(data)
  //   //   }
  //   // );
  //
  //
  //   console.log('afterCreate');
  // }

}
