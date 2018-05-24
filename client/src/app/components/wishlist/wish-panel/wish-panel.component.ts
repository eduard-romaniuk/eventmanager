import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-wish-panel',
  templateUrl: './wish-panel.component.html',
  styleUrls: ['./wish-panel.component.css']
})
export class WishPanelComponent implements OnInit {

  @Input()
  eventId: number;

  @Input()
  userId: number;

  constructor() { }

  ngOnInit() {
  }

}
