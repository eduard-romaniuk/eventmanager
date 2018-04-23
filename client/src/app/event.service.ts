import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class EventService {

  headers: HttpHeaders;

  constructor(private http: HttpClient) {
  }

  create(eventData, callback) {

    this.headers = new HttpHeaders(eventData? {eventData: JSON.stringify({
      eventName: eventData.eventName,
      description: eventData.description,
      place: eventData.place,
      timeLineStart: eventData.timeLineStart,
      timeLineFinish: eventData.timeLineFinish,
      period: eventData.period,
      isPrivate: eventData.isPrivate
    }) } : {});

    console.log(`CreateNewEvent(${this.headers.get("eventData")})`);



    this.http.post('http://localhost:8080/event', {headers: this.headers}).subscribe(response => {
      return callback && callback();
    });
  }

}
