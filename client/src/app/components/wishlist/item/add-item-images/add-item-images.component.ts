import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ToastService} from "../../../../services/toast.service";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../../model/user";
import {UserService} from "../../../../services/user.service";
import {ImageUploaderService} from "../../../../services/image-uploader.service";
import {imageExtension} from "../../../../utils/validation-tools";
import {Item} from "../../../../model/item";
import {CloudinaryOptions, CloudinaryUploader} from "ng2-cloudinary";

@Component({
  selector: 'app-add-item-images',
  templateUrl: './add-item-images.component.html',
  styleUrls: ['./add-item-images.component.css']
})
export class AddItemImagesComponent implements OnInit {

  @Input() item: Item;
  newImages: String[] = [];

  formImg: FormGroup;
  imageUploading = false;
  savingChanges = false;
  error = false;

  uploader: CloudinaryUploader

  constructor(private router: Router,
              private userService: UserService,
              private formBuilder: FormBuilder,
              private toast: ToastService) {

	this.uploader = new CloudinaryUploader(
    new CloudinaryOptions({
      cloudName: 'eventnetcracker',
      uploadPreset: 'tawlrxdf' })
	);

    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
      console.log("onSuccess: " + this.imageUploading);

	  this.imageUploading = false;
	  console.log(this.imageUploading);
      let res: any = JSON.parse(response);


      this.item.images.push(res.url);
      this.newImages.push(res.url);

	  console.log(this.item);
      console.log(`res - ` + JSON.stringify(res));
      return {item, response, status, headers};
    };
  }

  ngOnInit() {
	  console.log(this.item);

    this.formImg = this.formBuilder.group({
        image: ['', [Validators.required]]
      },
      {validator: imageExtension('image')}
    );
  }

  upload() {
    if (this.formImg.get("image").valid) {
      this.imageUploading = true;
	  console.log("upload:" + this.imageUploading);
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

  reset() {

	  this.newImages = [];
	  this.item.images = [];
	  this.imageUploading = false;
	  this.savingChanges = false;
	  this.error = false;

  }




}
