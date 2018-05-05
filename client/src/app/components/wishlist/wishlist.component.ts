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
export class WishListComponent implements OnInit {

  wishListId: number = 1;
  title = 'Your Wish Board!';
  items: Item[] = [];
  subscription: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private wishListService: WishListService,
              ) {
    this.subscription = this.wishListService.getViewingItem().subscribe(item => {});

  }

  ngOnInit() {
    this.wishListService.getItemsFromWishList(this.wishListId)
      .subscribe( (items : any) => {
        this.items = items;
        console.log(items);
      });
  }

  create() {
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
