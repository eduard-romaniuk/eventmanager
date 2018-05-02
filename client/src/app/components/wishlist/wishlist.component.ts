import { Component, OnInit } from '@angular/core';
import { WishListService } from '../../services/wishlist.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'
import {Item} from '../../model/item';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-wish-list',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent {

  title = 'Your Wish Board!';
  items: Item[] = [];
  subscription: Subscription;
  event : Event = new Event();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private wishListService: WishListService,
              ) {
    this.subscription = this.wishListService.getViewingItem().subscribe(item => {});
    this.sendViewingItem(this.items[0]);


    this.generateItem(
      1,
      "Backpack",
      "A backpack — also called bookbag, kitbag, knapsack, rucksack, rucksac, pack, sackpack or backsack — is, in its simplest form, a cloth sack carried on one's back and secured with two straps that go over the shoulders, but there can be variations to this basic design.",
      2,
      20,
      ['backpack', 'adventures', 'life', 'nature', 'mountains']
    );

    this.generateItem(
      2,
      "Watch",
      "A watch is a timepiece intended to be carried or worn by a person. It is designed to keep working despite the motions caused by the person's activities. A wristwatch is designed to be worn around the wrist, attached by a watch strap or other type of bracelet. A pocket watch is designed for a person to carry in a pocket.",
      1,
      44,
      ['watch', 'in-my-hand', 'future', 'nature']
    );

    this.generateItem(
      3,
      "Book",
      "A good book description is detailed, descriptive copy that is good for public display, used for your book marketing, book discovery, and for sales purposes. It helps potential buyers find and understand your book. It's your pitch.",
      2,
      10,
      ['book', 'looks', 'so', 'clever']
    );

    this.generateItem(
      4,
      "Cup",
      "Tea is an aromatic beverage commonly prepared by pouring hot or boiling water over cured leaves of the Camellia sinensis, an evergreen shrub (bush) native to Asia.",
      0,
      99,
      ['cup', 'tea', 'like', 'China']
    );



  }

  create() {
  }

  generateItem (id: number, name: string, description: String, priority: number, likes: number, tags: String[]){
    let item1: Item = new Item();
    item1.id = id;
    item1.descriprion = description;
    item1.name = name;
    item1.tags = tags;
    item1.likes = likes;
    item1.priority = priority;

    this.items.push(item1)
  }

  sendViewingItem(item: Item){
    this.wishListService.sendViewingItem(item);
  }

  hideViewingItem(){
    this.wishListService.hideViewingItem()
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}
