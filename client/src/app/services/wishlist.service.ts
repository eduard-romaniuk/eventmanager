import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Item} from '../model/item';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {Event} from "../model/event";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class WishListService {

  headers: HttpHeaders;
  private base_url = '/wishlist';
  private subject=new Subject<Item>();
  private subItemArr = new BehaviorSubject<Item[]>([]);
  private items: Item[] = [];

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
    this.http.get<Item[]>(this.base_url + "/" + wishListId).subscribe( (items : Item[]) => {
      this.items = items;
      this.subItemArr.next(this.items);
    });

    return this.subItemArr.asObservable();

  }

  addItemIntoArr(item: Item) : void {
    this.items.push(item);
    this.subItemArr.next(this.items);
  }

}
