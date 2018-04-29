import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Event } from '../model/event';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class WishListService {

  headers: HttpHeaders;
  private base_url = '/wishlist';

  constructor(private http: HttpClient) {
  }

  public createEvent(event) {
    console.log('Create event-wishlist');
    console.log(event);
    return this.http.post(this.base_url, event).subscribe(
      (data:any) => {
        console.log(data);
      }
    );
  }

}
