import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';
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

export function dateValidator(from:string){
  return (group: FormGroup): {[key: string]: any} => {
    let f = new Date(group.controls[from].value);
    let now = new Date();
    console.log(f);
    console.log(now);
    if (f < now) {
      group.get(from).setErrors({ dateValidator: true });
    }
    return null;
  }
}
export function dateLessThan(from: string, to: string) {
  return (group: FormGroup): {[key: string]: any} => {
    let f = group.controls[from];
    let t = group.controls[to];
    console.log(f.value);
    console.log(t.value);
    if (f.value > t.value) {
      group.get(to).setErrors({ dateLessThan: true });
    }
    return null;
  }
}
