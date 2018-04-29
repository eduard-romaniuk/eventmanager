import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Router, ActivatedRoute} from '@angular/router';
import { JQueryStatic } from 'jquery';

import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ItemService} from '../../../../services/item.service';
import {Item} from '../../../../model/item';

  @Component({
    selector: 'app-create-item',
    templateUrl: './createItem.component.html',
    styleUrls: ['./createItem.component.css']
  })
  export class CreateItemComponent implements OnInit{
  form: FormGroup;
  item: Item = new Item();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private itemService: ItemService,
              private formBuilder: FormBuilder
  ) { }

  ngOnInit(){
    this.initForm();
  }

  initForm(){
    this.form = this.formBuilder.group({
      name: ['',
        [Validators.required,
        Validators.maxLength(45)]
      ],

      description: [''],

      // priority: [0]


    });
  }


  create() {
    //TODO: realization
  }

}
