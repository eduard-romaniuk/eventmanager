import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';


export function passConfirm(form: AbstractControl) {
  if (form.get('password').value !== form.get('confirmPassword').value) {
    form.get('confirmPassword').setErrors({ passconfirm: true });
  } else {
    return null;
  }
}

export function imageExtension(form: AbstractControl) {
  var validExtensionsForImage = ["jpg", "jpeg", "png", "gif"];
  var imageName = form.get('image').value;
  var extension = imageName.split(".").pop();

  if (validExtensionsForImage.indexOf(extension) < 0) {
    form.get('image').setErrors({ imageExtension: true });
  } else {
    return null;
  }
}

// TODO: check for boolean
export function boolean(control: AbstractControl) {
  return control.value === '' ? { boolean: true } : null;
}

export function dateValidator(): ValidatorFn {
   return (control: AbstractControl): {[key: string]: any} => {

     let now = new Date();
     let invalidObj = { 'date': true };

     let date = new Date(control.value);
       if (date <= now) {
            return invalidObj;
          }
       return null;
     }
  }

export function dateLessThan(from: string, to: string) {
  return (group: FormGroup): {[key: string]: any} => {
    let f = group.controls[from];
    let t = group.controls[to];

    if (f.value > t.value) {
      group.get(to).setErrors({ dateLessThan: true });
    }
    return null;
  }
}
