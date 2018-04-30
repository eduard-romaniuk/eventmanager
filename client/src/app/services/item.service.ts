import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {Item} from '../model/item';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class ItemService {

  headers: HttpHeaders;
  private base_url = '/item';

  constructor(private http: HttpClient) {
  }

  public createItem(item: Item, callback?, errorCallback?) {
    // console.log('Create event-wishlist');
    // console.log(event);
    this.http.post(this.base_url + '/', item).subscribe(
      response => {
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      })

    //TODO: delete after adding backend
    return callback && callback();
  }

}
