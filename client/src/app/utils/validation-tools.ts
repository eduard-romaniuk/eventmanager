import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';
import {UserService} from "../services/user.service";

export function usernameExists(userService: UserService, currentUsername: string) {
  return (control: AbstractControl) => {
    return userService.isUsernameExists(control.value).map(response => {
      if(response){
        return control.value !== currentUsername ? { usernameExists: true } : null;
      } else {
        return null;
      }
    });
  };
}

export function emailExists(userService: UserService) {
  return (control: AbstractControl) => {
    return userService.isEmailExists(control.value).map(response =>
    {return response ? { emailExists: true } : null;})
  };
}

export function phoneLength(phone: string) {
  return (group: FormGroup): { [key: string]: any } => {
    let phoneNumber = group.controls[phone];
    console.log("phoneNumber - " + phoneNumber.value);
    if(phoneNumber.value){
      if (phoneNumber.value.length < 10 || phoneNumber.value.length > 18) {
        group.get(phone).setErrors({phoneLength: true});
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}

export function passConfirm(password: string, passwordConfirmation: string) {
  return (group: FormGroup): { [key: string]: any } => {
    let pass = group.controls[password];
    let passConf = group.controls[passwordConfirmation];

    if (pass.value !== passConf.value) {
      group.get(passwordConfirmation).setErrors({passconfirm: true});
    } else {
      return null;
    }
  }
}

export function imageExtension(image: string) {
  let validExtensionsForImage = ["jpg", "jpeg", "png", "gif"];
  return (group: FormGroup): { [key: string]: any } => {
    let img = group.controls[image];
    let extension = img.value.split(".").pop();

    if (validExtensionsForImage.indexOf(extension) < 0) {
      group.get(image).setErrors({imageExtension: true});
    } else {
      return null;
    }
  }
}

export function maxPeriod(eventStartDate: Date, notificationStartDate: string, period: string) {
  return (group: FormGroup): { [key: string]: any } => {
    let notifStartDate = group.controls[notificationStartDate];
    let per = group.controls[period];
    let maxPeriod;

    let maxNotificationDate = new Date();
    maxNotificationDate.setDate(eventStartDate.getDate());

    let notStartDate = new Date(notifStartDate.value);

    console.log("maxNotificationDate - " + maxNotificationDate);
    console.log("notStartDate - " + notStartDate);

    if (notifStartDate.value) {
      let diff = maxNotificationDate.getTime() - notStartDate.getTime();
      maxPeriod = Math.ceil(diff / (1000 * 3600 * 24));
    } else {
      maxPeriod = 0;
    }

    console.log("maxPeriod - " + maxPeriod);

    if (per.value > maxPeriod) {
      group.get(period).setErrors({maxPeriod: true});
    } else {
      return null;
    }
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
