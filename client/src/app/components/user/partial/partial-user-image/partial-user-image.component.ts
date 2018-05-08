import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/user";

@Component({
  selector: 'app-partial-user-image',
  templateUrl: './partial-user-image.component.html',
  styleUrls: ['./partial-user-image.component.css']
})
export class PartialUserImageComponent implements OnInit {

  @Input() user: User;

  constructor() { }

  ngOnInit() {
  }

}
