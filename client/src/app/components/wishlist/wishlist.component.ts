import { Component, OnInit } from '@angular/core';
import { WishListService } from '../../services/wishlist.service';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import { Event } from '../../model/event'

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent {

  title = 'Your Wish Board!';

  items = [
    'horse',
    'candies',
    'backPack',
    'sea'
  ];

  event : Event = new Event();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private wishlist: WishListService) { }

  create() {
    // TODO: Handle create error
    this.wishlist.createEvent(this.event);
  }

}
