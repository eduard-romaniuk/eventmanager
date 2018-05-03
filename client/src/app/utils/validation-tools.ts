import { AbstractControl } from '@angular/forms';
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


