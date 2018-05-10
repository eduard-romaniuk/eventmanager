import { Component, OnInit } from '@angular/core';
import {Item} from "../../../../model/item";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

import {ActivatedRoute, Router} from "@angular/router";
import {ItemService} from "../../../../services/item.service";
import {WishListService} from "../../../../services/wishlist.service";

@Component({
  selector: 'app-edit-item',
  templateUrl: './edit-item.component.html',
  styleUrls: ['./edit-item.component.css']
})
export class EditItemComponent implements OnInit {
  form: FormGroup;
  item: Item = new Item();
  userId: number;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private itemService: ItemService,
    private formBuilder: FormBuilder,
    private wishListService: WishListService
  ) { }

  ngOnInit() {

    this.wishListService.getViewingItem().subscribe(
      (item: Item) => this.item = item
    );

    this.initForm();
  }

  initForm(){
    this.form = this.formBuilder.group({
      name: ['',
        [Validators.required,
          Validators.maxLength(45)]
      ],

      description: [''],

      priority: [ 1 , [Validators.required]],

      // images: [null]

      // tags: this.formBuilder.array([this.createTag()])
      //TODO: item models

    });
  // }
  //
  // private createTag(): FormGroup {
  //   return this.formBuilder.group({
  //     name: ['', [Validators.required]]
  //   });
  }

  editItem(): void {

  }


}
