import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Item} from '../model/item';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {Event} from "../model/event";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class WishListService {

  headers: HttpHeaders;
  private base_url = '/wishlist';
  private subject=new Subject<Item>();

  constructor(private http: HttpClient) {
  }

  sendViewingItem(viewingItem: Item):void{
    this.subject.next(viewingItem);
  }

  hideViewingItem(){
    this.subject.next(new Item());
  }

  getViewingItem(): Observable<Item> {
    return this.subject.asObservable()
  }

  getItemsFromWishList( wishListId: number ) : Observable<Item[]> {
    return this.http.get<Item[]>(this.base_url + "/" + wishListId);
  }
}
