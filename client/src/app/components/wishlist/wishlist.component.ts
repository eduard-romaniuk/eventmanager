import { Component, OnInit } from '@angular/core';
import { WishListService } from '../../services/wishlist.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'
import {Item} from '../../model/item';
import {Subscription} from 'rxjs/Subscription';
import {LikeService} from "../../services/like.service";
import {ItemService} from "../../services/item.service";

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
              private likeService: LikeService,
              private itemService: ItemService
              ) {

  }

  ngOnInit() {

    this.subscription = this.wishListService.getViewingItem().subscribe(item => {});

    this.wishListService.getItemsFromWishList(this.wishListId)
      .subscribe( (items : any) => {
        this.items = items;
      });

    for ( let item of this.items){
      this.likeService.wasLiked(item.id).subscribe( (hasLike: boolean) => {
        item.hasLiked = hasLike;
      });
    }
  }

  create() {
  }

  sendViewingItem(item: Item){
    this.itemService.getItem(item.id).subscribe( (fullItem: Item) => {
      console.log(fullItem);
      this.wishListService.sendViewingItem(fullItem)
    } );
  }

  hideViewingItem(){
    this.wishListService.hideViewingItem()
  }

  clickOnLikeButt(item: Item){
    this.likeService.addLike(item);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}
