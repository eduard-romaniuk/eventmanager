import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {WishListService} from "../../../services/wishlist.service";
import {LikeService} from "../../../services/like.service";
import {UserService} from "../../../services/user.service";
import {ItemService} from "../../../services/item.service";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../../model/user";
import {WishList} from "../../../model/wishlist";
import {WishListComponent} from "../wishlist.component";

@Component({
  selector: 'app-wish-panel',
  templateUrl: './wish-panel.component.html',
  styleUrls: ['./wish-panel.component.css']
})
export class WishPanelComponent implements OnInit {
  @Input()
  eventId: number;

  @Input()
  userId: number;

  @ViewChild("wish1")
  wish1: WishListComponent;

  @ViewChild("wish1")
  wish2: WishListComponent;

  constructor( private wishListService: WishListService) { }

  ngOnInit() {
  }

  toUserBooking() {
	if(this.userId){
		this.wish1.initWishList(this.userId);
	}
  }

  toEventBooking() {
    this.wish2.initEventsBookedItems();
  }
}
