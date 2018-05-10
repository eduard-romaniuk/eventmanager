import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/user";

@Component({
  selector: 'app-partial-user-list',
  templateUrl: './partial-user-list.component.html',
  styleUrls: ['./partial-user-list.component.css']
})
export class PartialUserListComponent implements OnInit {

  @Input() currentUser: User = new User();
  @Input() user: User = new User();

  constructor() { }

  ngOnInit() {
  }

}
