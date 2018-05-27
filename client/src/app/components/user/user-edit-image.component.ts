import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";

import {CloudinaryUploader} from 'ng2-cloudinary';
import {ImageUploaderService} from "../../services/image-uploader.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {imageExtension} from "../../utils/validation-tools";
import {ToastService} from '../../services/toast.service';

@Component({
  selector: 'app-user-edit-image',
  templateUrl: './user-edit-image.component.html',
  styleUrls: ['./user-edit-image.component.css']
})
export class UserEditImageComponent implements OnInit {

  @Input() user: User = new User();

  form: FormGroup;
  imageUploading = false;
  savingChanges = false;
  error = false;

  uploader: CloudinaryUploader = ImageUploaderService.getUploader();

  constructor(private router: Router,
              private userService: UserService,
              private formBuilder: FormBuilder,
              private toast: ToastService) {
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      this.imageUploading = false;
      let res: any = JSON.parse(response);
      this.user.image = res.url;
      console.log(`res - ` + JSON.stringify(res));
      return {item, response, status, headers};
    };
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
        image: ['', [Validators.required]]
      },
      {validator: imageExtension('image')}
    );
  }

  upload() {
    if (this.form.get("image").valid) {
      this.imageUploading = true;
      this.uploader.uploadAll();
    }
  }

  updateUser(toastMessage){
    this.savingChanges = true;
    this.userService.updateUser(this.user).subscribe((user: any) => {
      this.savingChanges = false;
      this.goHome();
      this.toast.success(toastMessage);
    }, error => {
      console.error(error);
      this.error = true;
      this.savingChanges = false;
    });
  }

  deleteImage() {
    this.user.image = null;
    this.updateUser('Profile image successfully deleted');
  }

  uploadImage() {
    this.updateUser('Profile image successfully updated');
  }

  goHome(): void {
    this.router.navigate(['users', this.user.id]);
  };

}
