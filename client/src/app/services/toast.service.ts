import { Injectable } from '@angular/core';

@Injectable()
export class ToastService {

  constructor() {}

  public info(message: string) {
    console.log(`info: ${message}`);
  }

  public error(message: string) {
    console.log(`error: ${message}`);
  }

  public warn(message: string) {
    console.log(`warn: ${message}`);
  }

  public success(message: string) {
    console.log(`success: ${message}`);
  }
}
