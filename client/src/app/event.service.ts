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

    //this.http.post('http://localhost:8080/event/createEvent', {headers: this.headers});

    this.http.post(`http://localhost:80/event`,
      JSON.stringify({
        eventName: eventData.eventName,
        description: eventData.description,
        place: eventData.place,
        timeLineStart: eventData.timeLineStart,
        timeLineFinish: eventData.timeLineFinish,
        period: eventData.period,
        isPrivate: eventData.isPrivate
      }))
      .subscribe(
        (data:any) => {
          console.log(data);
        }
      )

    console.log('afterCreate');
  }

}
