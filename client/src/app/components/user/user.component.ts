import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../../model/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User = new User();

  sub: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.userService.getUserById(id).subscribe((user: any) => {
          if (user) {
            this.user = user;
          } else {
            console.log(`User with id '${id}' not found!`);
            this.gotoList();
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  goToEditUserPage(user: User): void {
    this.router.navigate(['users', user.id, 'edit']);
  };

  gotoList() {
    this.router.navigate(['/users']);
  }

}
