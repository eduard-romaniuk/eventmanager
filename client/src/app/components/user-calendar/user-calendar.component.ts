import { Component, ChangeDetectionStrategy, Input, OnInit } from '@angular/core';
import { CalendarMonthViewDay, CalendarEvent } from 'angular-calendar';
import { EventService } from '../../services/event.service';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators';

const colors: any = {
  color: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  }
};

@Component({
  selector: 'app-user-calendar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './user-calendar.component.html',
  styleUrls: ['./user-calendar.component.css']
})
export class UserCalendarComponent implements OnInit {

  @Input() user_id: number;

  view: string = 'month';
  viewDate: Date = new Date();

  month = this.getMonthRange(new Date());

  data: Observable<Object>;

  events$: Observable<Array<CalendarEvent<{ public: number, low: number, normal: number, urgent: number }>>>

  constructor(private eventService: EventService) {}

  ngOnInit() {
  	this.fetchEvents();
  }

  prevMonth() {
  	var start = this.month[0];
  	start.setMonth(start.getMonth() - 1);
  	this.month = this.getMonthRange(start);
  	this.fetchEvents();
  }

  nextMonth() {
  	var start = this.month[0];
  	start.setMonth(start.getMonth() + 1);
  	this.month = this.getMonthRange(start);
  	this.fetchEvents();
  }

  fetchEvents() {
  	this.events$ = this.eventService
  		.getCalendarData(this.month[0].getFullYear(), this.month[0].getMonth(), this.user_id)
  		.pipe(
  			map(results => {
  				const object = Object.entries(results);
  				return object.map(entry => {
  					return {
  						title: '',
  						start: new Date(this.month[0].getFullYear(), this.month[0].getMonth(), Number(entry[0])),
  						color: colors.color,
  						meta: {
  							low: 	entry[1][0],
  							low_link: 	'/users/' + this.user_id + '/events/low/' + this.getDayRange(new Date(this.month[0].getFullYear(), this.month[0].getMonth(), Number(entry[0]))),
  							normal: entry[1][1],
  							normal_link: '/users/' + this.user_id + '/events/normal/' + this.getDayRange(new Date(this.month[0].getFullYear(), this.month[0].getMonth(), Number(entry[0]))),
  							urgent: entry[1][2],
  							urgent_link: '/users/' + this.user_id + '/events/urgent/' + this.getDayRange(new Date(this.month[0].getFullYear(), this.month[0].getMonth(), Number(entry[0]))),
  							public: entry[1][3],
  							public_link: '/events/list/' + this.getDayRange(new Date(this.month[0].getFullYear(), this.month[0].getMonth(), Number(entry[0])))
  						}
  					}
  				})
  			}))
  }

  beforeMonthViewRender({ body }: { body: CalendarMonthViewDay[] }): void {
    body.forEach(cell => {
      cell.events.forEach((event: CalendarEvent<{ low: number, normal: number, urgent: number, public: number,
      							low_link: string, normal_link: string, urgent_link: string, public_link: string }>) => {
      	cell['low'] = event.meta.low;
      	cell['normal'] = event.meta.normal;
      	cell['urgent'] = event.meta.urgent;
      	cell['public'] = event.meta.public;


      	cell['low_link'] = event.meta.low_link;
      	cell['normal_link'] = event.meta.normal_link;
      	cell['urgent_link'] = event.meta.urgent_link;
      	cell['public_link'] = event.meta.public_link;
      });
    });
  }

  getMonthRange(d: Date) {
	  return [
	      new Date(d.getFullYear(), d.getMonth(), 1, 0, 1),
	      new Date(d.getFullYear(), d.getMonth() + 1, 0, 23, 59)
	  ];
  }

  getDayRange(d: Date) {
	return new Date(d.getFullYear(), d.getMonth(), d.getDate(), 0, 1).toISOString().substr(0, 22) + "|" +
	       new Date(d.getFullYear(), d.getMonth(), d.getDate(), 23, 59).toISOString().substr(0, 22)
  }

}