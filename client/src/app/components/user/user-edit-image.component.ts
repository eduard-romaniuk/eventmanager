import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Subscription} from "rxjs/Subscription";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";

import {CloudinaryUploader} from 'ng2-cloudinary';
import {ImageUploaderService} from "../../services/image-uploader.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {imageExtension} from "../../utils/validation-tools";

@Component({
  selector: 'app-user-edit-image',
  templateUrl: './user-edit-image.component.html',
  styleUrls: ['./user-edit-image.component.css']
})
export class UserEditImageComponent implements OnInit, OnDestroy {

  user: User = new User();
  sub: Subscription;

  form: FormGroup;
  imageUploading = false;
  savingChanges = false;
  error = false;

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private formBuilder: FormBuilder) {
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      this.imageUploading = false;
      let res: any = JSON.parse(response);
      this.user.image = res.url;
      console.log(`res - ` + JSON.stringify(res) );
      return { item, response, status, headers };
    };
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
        image: ['', [ Validators.required ]]},
      {validator: imageExtension}
    );

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
    if(this.form.get("image").valid){
      this.imageUploading = true;
      this.uploader.uploadAll();
    }
  }

  uploadImage() {
    this.savingChanges = true;
    this.userService.updateUser(this.user).subscribe((user: any) => {
      this.savingChanges = false;
      this.goHome();
    }, error => {
      console.error(error);
      this.error = true;
      this.savingChanges = false;
    });
  }

  goHome(): void {
    this.router.navigate(['users', this.user.id]);
  };

}
