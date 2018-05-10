import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {Item} from '../model/item';
import {WishListService} from "./wishlist.service";
import {Observable} from "rxjs/Observable";
import {Event} from "../model/event";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class ItemService {

  headers: HttpHeaders;
  private base_url = '/item';

  constructor(private http: HttpClient, private wishListService: WishListService) {
  }

  public createItem(item: Item, callback?, errorCallback?) {
    console.log(item);
    this.http.post(this.base_url + '/', item).subscribe(
      (item: Item) => {
        if (item != null){
          this.wishListService.addItemIntoArr(item);
        }
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      });
  }

  public getItem( itemId: number ): Observable<Item> {
    return this.http.get<Item>(this.base_url + "/" + itemId);
  }

  public editItem(item: Item, callback?, errorCallback?) {
    console.log(item);
    this.http.post(this.base_url + '/' + item.id, item).subscribe(
      (item: Item) => {
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      });
  }



}
