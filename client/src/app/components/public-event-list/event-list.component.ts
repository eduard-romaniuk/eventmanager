import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { User } from '../../model/user';
import { Event } from '../../model/event';
import { Category } from '../../model/category';
import { EventService } from '../../services/event.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-public-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.css']
})
export class PublicEventListComponent implements OnInit {

  index = 10;
  count = 0;
  events: Event[] = [];

  pattern: string = '';
  last_pattern: string = this.pattern;

  categories: Category[] = [{id: 0, name: 'All'}]

  category: string = this.categories[0].name;
  last_category: string = this.category;

  today: Date = new Date();
  date_range = [
      new Date(this.today.getFullYear(), this.today.getMonth(), 1, 0, 1),
      new Date(this.today.getFullYear(), this.today.getMonth() + 1, 0, 23, 59)
  ];
  last_date_range: Date[] = this.date_range;

  constructor(private router: Router, private route: ActivatedRoute,
    private eventService: EventService, private toast: ToastService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if(params['date']) {
        this.date_range = params['date'] !== '-' ? [
          new Date(params['date'].substr(0, params['date'].indexOf("|"))),
          new Date(params['date'].substr(params['date'].indexOf("|") + 1))
        ] : [
        new Date(this.today.getFullYear(), this.today.getMonth(), 1, 0, 1),
        new Date(this.today.getFullYear(), this.today.getMonth() + 1, 0, 23, 59)
        ];
        this.last_date_range = this.date_range;
      }
      this.eventService.getCategories().subscribe(response => {
          this.categories = this.categories.concat(response);
      });
      this.reload();
    });

  }


  reload() {
    console.log(`${this.last_date_range[0].toISOString()}`);
    this.eventService.getFilteredEvents(this.last_pattern, this.last_category === 'All' ? '' : this.last_category, this.last_date_range[0], this.last_date_range[1], 10, 0).subscribe(
      response => {
        this.count = +response.headers.get('count');
        this.events = response.body;
      }, error => {
        console.log(error);
        this.toast.error('Some errors occurred while trying to load data');
      })
  }

  loadMore() {
    this.eventService.getFilteredEvents(this.last_pattern, this.last_category === 'All' ? '' : this.last_category, this.last_date_range[0], this.last_date_range[1], 10, this.index).subscribe(
      response => {
        this.index += 10;
        this.events = this.events.concat(response.body);
      }, error => {
        this.toast.error('Some errors occurred while trying to load more data');
      })
  }

  canLoadMore() {
    return this.index < this.count;
  }

  search() {
    if(this.pattern !== this.last_pattern){
      this.last_pattern = this.pattern;
      this.reload();
    }
  }

  filter() {
    if(this.date_range !== this.last_date_range){
      this.last_date_range = this.date_range;
      this.reload();
    }
    if(this.category !== this.last_category){
      this.last_category = this.category;
      this.reload();
    }
  }

}
