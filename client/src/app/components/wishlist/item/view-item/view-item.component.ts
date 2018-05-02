import {Component, Input, OnInit} from '@angular/core';
import {Item} from '../../../../model/item';
import {WishListService} from '../../../../services/wishlist.service';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-view-item',
  templateUrl: './view-item.component.html',
  styleUrls: ['./view-item.component.css']
})
export class ViewItemComponent implements OnInit {

  item: Item;
  subscription: Subscription;

  constructor(
    private wishListService: WishListService
  ){
    this.ngOnInit();

    this.subscription = this.wishListService.getViewingItem().subscribe(item => {this.item = item;});
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }


}

