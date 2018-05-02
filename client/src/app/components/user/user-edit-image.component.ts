import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Subscription} from "rxjs/Subscription";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";

import {CloudinaryUploader} from 'ng2-cloudinary';
import {ImageUploaderService} from "../../services/image-uploader.service";

@Component({
  selector: 'app-user-edit-image',
  templateUrl: './user-edit-image.component.html',
  styleUrls: ['./user-edit-image.component.css']
})
export class UserEditImageComponent implements OnInit, OnDestroy {

  user: User = new User();
  sub: Subscription;

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService) {
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      let res: any = JSON.parse(response);
      this.user.image = res.url;
      console.log(`res - ` + JSON.stringify(res) );
      return { item, response, status, headers };
    };
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.userService.getUserById(id).subscribe((user: any) => {
          if (user) {
            this.user = user;
          } else {
            console.log(`User with id '${id}' not found!`);
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  upload() {
    this.uploader.uploadAll();
  }

  uploadImage() {
    console.log("this.user - " + this.user);
    this.userService.updateUser(this.user).subscribe((user: any) => {
      this.goHome();
    }, error => console.error(error));
  }

  goHome(): void {
    this.router.navigate(['users', this.user.id]);
  };

}
