import {Component, Input, OnInit} from '@angular/core';
import {Item} from '../../../../model/item';
import {WishListService} from '../../../../services/wishlist.service';
import {Subscription} from 'rxjs/Subscription';
import {AuthService} from "../../../../services/auth.service";
import {LikeService} from "../../../../services/like.service";
import {ItemService} from "../../../../services/item.service";

@Component({
  selector: 'app-view-item',
  templateUrl: './view-item.component.html',
  styleUrls: ['./view-item.component.css']
})
export class ViewItemComponent implements OnInit {
  item: Item;
  subscription: Subscription;
  userId: number;

  constructor(
    private wishListService: WishListService,
    private auth : AuthService,
    private likeService : LikeService,
    private itemService: ItemService
  ){
 }

  ngOnInit(): void {

    this.subscription = this.wishListService.getViewingItem().subscribe(item => {
      this.item = item;
      this.likeService.wasLiked(this.item.id).subscribe( (hasLike: boolean) => {
          this.item.hasLiked = hasLike;
        });
    });

    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;
    });

  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  clickOnLikeButt(): void {
    this.likeService.addLike(this.item);
  }

  public copyItem(): void {
    this.itemService.copyItem(this.item);
  }

  public deleteItem(): void {
    this.itemService.deleteItem(this.item)
  }


}

