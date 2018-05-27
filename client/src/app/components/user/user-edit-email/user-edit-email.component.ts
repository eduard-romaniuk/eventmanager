import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../model/user";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastService} from "../../../services/toast.service";
import {UserService} from "../../../services/user.service";
import {emailExists} from "../../../utils/validation-tools";

@Component({
  selector: 'app-user-edit-email',
  templateUrl: './user-edit-email.component.html',
  styleUrls: ['./user-edit-email.component.css']
})
export class UserEditEmailComponent implements OnInit {

  @Input() user: User = new User();

  form: FormGroup;
  formContent = {currentEmail: '', newEmail: ''};

  savingChanges = false;
  wrongEmailError = false;
  error = false;

  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private toast: ToastService) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
        currentEmail: ['', [ Validators.required, Validators.email]],
        newEmail: ['', [ Validators.required, Validators.email], emailExists(this.userService)]
      });
  }

  save() {
    this.wrongEmailError = false;
    this.error = false;
    this.savingChanges = true;

    this.userService.updateUserEmail(this.user.id, this.formContent.currentEmail, this.formContent.newEmail)
      .subscribe(response => {
        this.savingChanges = false;
        this.form.reset();
        this.toast.info('We send a confirmation letter to your new email address');
        }, error => {
          if (error.status === 409) {
            this.savingChanges = false;
            this.wrongEmailError = true;
          } else {
            this.savingChanges = false;
            this.error = true;
          }
        }
      );
  }

}
