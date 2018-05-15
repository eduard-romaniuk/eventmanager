import { Component, OnInit } from '@angular/core';
import {Item} from "../../../../model/item";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";

import {ActivatedRoute, Router} from "@angular/router";
import {ItemService} from "../../../../services/item.service";
import {WishListService} from "../../../../services/wishlist.service";
import {Tag} from "../../../../model/tag";
import { JQueryStatic } from 'jquery';
declare var $:JQueryStatic;

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

    this.initForm();

    this.wishListService.getViewingItem().subscribe(
      (item: Item) => {
        if (item) {

          this.item = item;
          this.setDataIntoForm();

        } else {
          console.log(`Cannot get viewing item!`);
        }
      }
    );

  }

  initForm(){
    this.form = this.formBuilder.group({
      name: [this.item.name,
        [Validators.required,
          Validators.maxLength(45)]
      ],

      description: [this.item.description],

      priority: [ this.item.priority , [Validators.required]],

      // images: [null]

      tags: this.formBuilder.array([this.initTag()])


    });
  }

  private initTag(): FormGroup {
    return this.formBuilder.group({
      name: ['', [Validators.required]]
    });
  }

  private setDataIntoForm(): void {
    this.form.patchValue({
      name: this.item.name,
      description: this.item.description,
      priority: this.item.priority,
    });

    this.addTags()
  }

  private addTags(): FormArray {

    let tags = <FormArray>this.form.controls["tags"];
    while (tags.length !== 0) {
      tags.removeAt(0)
    }

    for (let tag of this.item.tags) {
      tags.push(this.formBuilder.group({ name: [tag.name, [Validators.required] ]}));
    }

    tags.push(this.formBuilder.group({ name: ["", [Validators.required] ]}))

    return tags;
  }

  addTag(): void {
    (<FormArray>this.form.get("tags")).push(this.formBuilder.group({ name: ["", [Validators.required] ]}));
  }

  removeTag(i: number): void {
    (<FormArray>this.form.get("tags")).removeAt(i);
  }

  editItem(): void {
    const arr: FormArray = <FormArray>this.form.get("tags");
    this.item.tags = [];

    for (let i = 0; i < arr.length-1; i++){
      var tag = new Tag();
      tag.name = <String>arr.at(i).get("name").value
      this.item.tags.push(tag);
    }

    this.item.name = <String>this.form.get("name").value;
    this.item.description = <String>this.form.get("description").value;
    this.item.priority = <number>this.form.get("priority").value;

    this.itemService.editItem(
      this.item,

      () => {
        $('#editItem').modal('hide');
        $('#viewItem').modal('show')
        this.form.reset();
        this.form.setControl('tags', new FormArray([]));
      }
    )
  }

  get formData() { return <FormArray>this.form.get('tags'); }




}
