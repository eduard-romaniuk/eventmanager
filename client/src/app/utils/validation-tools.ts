import {AbstractControl, ValidatorFn} from '@angular/forms';
import { AuthService } from '../services/auth.service';


export function passConfirm(form: AbstractControl) {
  if (form.get('password').value !== form.get('confirmPassword').value) {
    form.get('confirmPassword').setErrors({ passconfirm: true });
  } else {
    return null;
  }
}

// TODO: check for boolean
export function boolean(control: AbstractControl) {
  return control.value === '' ? { boolean: true } : null;
}

export function dateValidator(now): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} => {
    const dateStr = control.value;


    console.log("1",dateStr);
    console.log("2",now);
    const invalidObj = { 'date': true };

    const date = new Date(dateStr);
    if (date <= now) {
      return invalidObj;
    }
    return null;
  }
}
