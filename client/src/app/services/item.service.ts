import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {Item} from '../model/item';
import {WishListService} from "./wishlist.service";
import {Observable} from "rxjs/Observable";
import {Event} from "../model/event";
import {ToastService} from "./toast.service";
import {Booker} from "../model/booker";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class ItemService {

  headers: HttpHeaders;
  private base_url = '/item';

  constructor(
    private http: HttpClient,
    private wishListService: WishListService,
    private toastService: ToastService
    ) {
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
        if (item != null){
          this.wishListService.changeItemFromArr(item);
        }
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      });
  }

  public copyItem (item: Item) {
    this.http.post(this.base_url + '/copy/' + item.id, this.wishListService.getCurrentWishListId()).subscribe(
      (itemId: number) => {console.log("copy item. Created new item with id: " + itemId)}
    );

    this.toastService.success("The item has been copied to your wish board");
  }

  public deleteItem (item: Item, callback?, errorCallback?) {
    this.http.delete(this.base_url + '/' + item.id ).subscribe(
    );

    this.wishListService.deleteItemFromArr(item.id);


    this.toastService.info("Item removed");
    return callback && callback();

  }

  public booking (booker: Booker,  callback?, errorCallback?) {
    this.http.post(this.base_url + '/booking', booker).subscribe(
      (booker: Booker) => {
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      }
    );
  }

  public unbooking (booker: Booker) {
    this.http.delete(this.base_url + "/booking/" + booker.itemId + "/" + booker.eventId + "/" + booker.userId).subscribe(
    );
  }

  public isBooked (booker: Booker): Observable<Boolean> {
    return this.http.get<boolean>(this.base_url + `/booking?user=${booker.userId}&event=${booker.eventId}&item=${booker.itemId}`)
  }



}
