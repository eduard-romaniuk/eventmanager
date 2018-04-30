import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/user";

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

  searchUserByLogin: string;
  users: User[];

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getUsers()
      .subscribe( (users : any) => {
        this.users = users;
      });
  }

}
