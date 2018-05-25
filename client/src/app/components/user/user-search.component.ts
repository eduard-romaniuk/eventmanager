import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/user";
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import {AuthService} from "../../services/auth.service";
import {ToastService} from "../../services/toast.service";

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

  currentUser: User = new User();
  users: User[] = [];

  index = 10;
  count = 0;

  queryString: string = '';

  constructor(private userService: UserService,
              private auth: AuthService,
              private toast: ToastService) {
    this.auth.current_user.subscribe(
      current_user => {
        this.currentUser = current_user;
        this.getAll();
      });
  }

  ngOnInit() {}

  getAll(){
    this.index = 10;
    this.userService.getUsersPagination(10, 0).subscribe(
      response => {
        this.count = +response.headers.get('count');
        this.users = response.body;
      }, error => {
        console.log(error);
        this.toast.error('Some errors occurred while trying to load data');
      })
  }

  search() {
    this.index = 10;
    this.userService.searchByLoginOrByNameAndSurnamePagination(this.queryString,
        10, 0).subscribe(
      response => {
        this.count = +response.headers.get('count');
        this.users = response.body;
      }, error => {
        console.log(error);
        this.toast.error('Some errors occurred while trying to load data');
      })
  }

  loadMore() {
    this.userService.searchByLoginOrByNameAndSurnamePagination(this.queryString,
      10, this.index).subscribe(
      response => {
        this.index += 10;
        this.users = this.users.concat(response.body);
      }, error => {
        this.toast.error('Some errors occurred while trying to load more data');
      })
  }

  canLoadMore() {
    return this.index < this.count;
  }

}
