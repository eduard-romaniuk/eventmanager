import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/user";
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

  currentUser: User = new User();
  users: User[];
  loginToSearch = new Subject<string>();

  constructor(private userService: UserService,
              private auth: AuthService) {
    this.auth.current_user.subscribe(
      current_user => {
        this.currentUser = current_user;
      });
  }

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
