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
import {UserService} from "../../../../services/user.service";
import {Booker} from "../../../../model/booker";
import {FormArray} from "@angular/forms";
import {User} from "../../../../model/user";
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

  bookersAsUsers: User[];

  currentBooker: Booker;

  @Input()
  eventId: number;


  constructor(
    private wishListService: WishListService,
    private auth : AuthService,
    private router: Router,
    private likeService : LikeService,
    private itemService: ItemService,
    private eventService: EventService,
	private userService: UserService
  ){
 }

  ngOnInit(): void {

    if (this.router.url == '/wishlist') this.isOwn = true;

    this.subscription = this.wishListService.getViewingItem().subscribe(item => {
      this.item = item;
      this.updateBookersName();
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
  

  hideAndShow() {

	$('#viewItem').modal('hide');
	$('#editItem').modal('show');

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

  updateBookersName(): void {
	this.bookersAsUsers = [];
	for (let booker of this.item.bookers) {
		this.userService.getUserById(booker.userId).subscribe(
				(user: User) => {
					this.bookersAsUsers.push(user);
				}
			);
	}

  }

  ngOnDestroy(): void {
	$('#viewItem').modal('hide');
	$('#wishlist').modal('hide');
	if(this.subscription)this.subscription.unsubscribe();
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
			this.item.bookers.push(this.currentBooker);
        });
    }
  }
  unbooking() {

    if( this.isBooked ) {

      this.itemService.unbooking(
        this.currentBooker,
        () => {
			this.isBooked = false;

			for (let booker of this.item.bookers){

				if (booker.eventId == this.currentBooker.eventId && booker.userId == this.currentBooker.userId) {
					const index: number = this.item.bookers.indexOf(booker);
					if (index !== -1) {
						this.item.bookers.splice(index, 1);
					}
				}
			}

        }
      );

    }

  }

  getEventId() : number {
    return this.eventId;
  }

  isBookersExist(): boolean {
	if(!this.item) return false;
	if(!this.item.bookers) return false;
	return  this.item.bookers.length > 0;
  }





}

