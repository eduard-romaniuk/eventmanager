import {Component, Input, OnInit} from '@angular/core';
import {CloudinaryUploader} from "ng2-cloudinary";
import {Router} from "@angular/router";
import {ToastService} from "../../../../services/toast.service";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../../model/user";
import {UserService} from "../../../../services/user.service";
import {ImageUploaderService} from "../../../../services/image-uploader.service";
import {imageExtension} from "../../../../utils/validation-tools";
import {Item} from "../../../../model/item";

@Component({
  selector: 'app-add-item-images',
  templateUrl: './add-item-images.component.html',
  styleUrls: ['./add-item-images.component.css']
})
export class AddItemImagesComponent implements OnInit {

  @Input() item: Item = new Item();
  newImages: String[] = []

  formImg: FormGroup;
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
      this.item.images.push(res.url);
      this.newImages.push(res.url);
      console.log(`res - ` + JSON.stringify(res));
      return {item, response, status, headers};
    };
  }

  ngOnInit() {
    this.formImg = this.formBuilder.group({
        image: ['', [Validators.required]]
      },
      {validator: imageExtension('image')}
    );
  }

  upload() {
    if (this.formImg.get("image").valid) {
      this.imageUploading = true;
      this.uploader.uploadAll();
    }
  }


  clean () {
    for (let newImage of this.newImages ) {
      this.item.images = this.item.images.filter(image => image != newImage);
      this.newImages = [];
    }
  }

  remove (i : number) {

    this.newImages = this.newImages.filter( image => image != this.item.images[i] );

    if ( i > -1 ) {
      this.item.images.splice (i, 1);
    }
  }




}
