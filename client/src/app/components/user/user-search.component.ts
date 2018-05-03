import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/user";
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

  users: User[];
  loginToSearch = new Subject<string>();

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.searchUser(this.loginToSearch).subscribe( (users : any) => {
      this.users = users;
    });
  }

  searchUser(terms: Observable<string>) {
    return terms.distinctUntilChanged()
      .switchMap(term => this.userService.searchUserByLogin(term));
  }

}
