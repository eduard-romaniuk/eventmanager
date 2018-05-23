import {Component, Input, OnInit} from '@angular/core';
import {Item} from '../../../../model/item';
import {WishListService} from '../../../../services/wishlist.service';
import {Subscription} from 'rxjs/Subscription';
import {AuthService} from "../../../../services/auth.service";
import {LikeService} from "../../../../services/like.service";
import {ItemService} from "../../../../services/item.service";
import { JQueryStatic } from 'jquery';
import {Router} from "@angular/router";
import {EventService} from "../../../../services/event.service";
import {Booker} from "../../../../model/booker";
import {FormArray} from "@angular/forms";
declare var $:JQueryStatic;


@Component({
  selector: 'app-view-item',
  templateUrl: './view-item.component.html',
  styleUrls: ['./view-item.component.css']
})
export class ViewItemComponent implements OnInit {
  item: Item;
  subscription: Subscription;
  userId: number;
  isOwn: boolean = false;
  isParticipant: boolean = false;
  isBooked: boolean = false;

  currentBooker: Booker;

  @Input()
  eventId: number;


  constructor(
    private wishListService: WishListService,
    private auth : AuthService,
    private router: Router,
    private likeService : LikeService,
    private itemService: ItemService,
    private eventService: EventService
  ){
 }

  ngOnInit(): void {

    if (this.router.url == '/wishlist') this.isOwn = true;

    this.subscription = this.wishListService.getViewingItem().subscribe(item => {
      this.item = item;
      this.likeService.wasLiked(this.item.id).subscribe( (hasLike: boolean) => {
          this.item.hasLiked = hasLike;
        });

      this.auth.getUser().subscribe((user: any) => {
        this.userId = user.id;
        if (this.eventId) {
          this.initParticipantViewWishList();
        }
      });

    });



  }

  initParticipantViewWishList() {

      this.eventService.isParticipantRequest(this.eventId).subscribe((participation: String) => {
        if (participation) {
          this.isParticipant = (participation == "true");

          if(this.isParticipant){
            this.currentBooker = new Booker();
            this.currentBooker.eventId = this.eventId;
            this.currentBooker.itemId = this.item.id;
            this.currentBooker.userId = this.userId;
            this.itemService.isBooked(this.currentBooker).subscribe(
              (isBooked: boolean ) => {
                this.isBooked = isBooked;
              }
            );
          }

        } else {
          console.log(`Participation not found!`);
        }
      });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.item = new Item();
  }

  clickOnLikeButt(): void {
    this.likeService.addLike(this.item);
    this.wishListService.changeLikeFromArr(this.item);
  }

  public copyItem(): void {
    this.itemService.copyItem(this.item);
  }

  public deleteItem(): void {

    this.itemService.deleteItem(
      this.item,
      () => {
        $('#viewItem').modal('hide');
      }
    );
  }

  isOwnItem(): boolean {
    return this.isOwn;
  }

  booking() {

    if ( !this.isBooked ) {

      this.itemService.booking(
        this.currentBooker,
        () => {
          this.isBooked = true;
        }
        );
    }
  }
  unbooking() {

    if( this.isBooked ) {

      this.itemService.unbooking(
        this.currentBooker,
        () => {
          this.isBooked = false;
        }
      );

    }

  }

  getEventId() : number {
    return this.eventId;
  }


}

