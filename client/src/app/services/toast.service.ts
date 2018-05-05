import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class ToastService {

  constructor(private toastr: ToastrService) {}

  public info(message: string) {
    this.toastr.info(message);
  }

  public error(message: string) {
    this.toastr.error(message);
  }

  public warn(message: string) {
    this.toastr.warning(message);
  }

  public success(message: string) {
    this.toastr.success(message);
  }
}
