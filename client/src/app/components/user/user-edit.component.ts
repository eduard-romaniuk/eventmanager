import {Component, OnInit, Input} from '@angular/core';
import {Router} from '@angular/router';

import {User} from '../../model/user';
import {UserService} from '../../services/user.service';
import {ToastService} from "../../services/toast.service";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  @Input() user: User = new User();

  constructor(private router: Router,
              private userService: UserService,
              private toast: ToastService) { }

  ngOnInit() {}

  save() {
    this.userService.updateUser(this.user).subscribe((user: any) => {
      this.router.navigate(['users', user.id]);
      this.toast.success('Info successfully updated');
    }, error => console.error(error));
    // this.userService.updateUser(this.user);
    // this.router.navigate(['users', this.user.id]);
  }

}
