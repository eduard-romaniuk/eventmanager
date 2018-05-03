import { Injectable } from '@angular/core';
import {CloudinaryOptions, CloudinaryUploader} from "ng2-cloudinary";

@Injectable()
export class ImageUploaderService {

  private static CLOUD_NAME = 'eventnetcracker';
  private static UPLOAD_PRESET = 'tawlrxdf';

  private static UPLOADER: CloudinaryUploader = new CloudinaryUploader(
    new CloudinaryOptions({
      cloudName: ImageUploaderService.CLOUD_NAME,
      uploadPreset: ImageUploaderService.UPLOAD_PRESET })
  );

  public static getUploader() : CloudinaryUploader {
    return ImageUploaderService.UPLOADER;
  }

}
