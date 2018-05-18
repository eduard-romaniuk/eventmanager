import {Component, Input, OnInit} from '@angular/core';
import {Event} from "../../model/event";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationSettings} from "../../model/notificationSettings";
import {NotificationSettingsService} from "../../services/notification-settings.service";
import {ToastService} from "../../services/toast.service";

@Component({
  selector: 'app-notification-settings',
  templateUrl: './notification-settings.component.html',
  styleUrls: ['./notification-settings.component.css']
})
export class NotificationSettingsComponent implements OnInit {

  @Input() userId: number;
  @Input() event: Event = new Event();
  @Input() notificationSetting: NotificationSettings = new NotificationSettings();

  form: FormGroup;
  savingChanges = false;
  error = false;

  min = new Date();
  max: Date;

  constructor(private formBuilder: FormBuilder,
              private notificationSettingsService: NotificationSettingsService,
              private toast: ToastService) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      emailNotif: [this.notificationSetting.emailNotificationOn, [Validators.required]],
      startDate: [this.notificationSetting.startDate, []],
      // startDate: new FormControl({value: this.notificationSetting.startDate,
      //   disabled: this.isDisabled})
      period: [this.notificationSetting.period, []],
      countDownOn: [this.notificationSetting.countDownOn, []]
      }
    );
    this.max = this.event.timeLineStart;
  }

  save() {
    this.error = false;
    this.savingChanges = true;
    console.log("notificationSetting.emailNotificationOn - " + this.notificationSetting.emailNotificationOn);

    this.notificationSettingsService.updateByUserIdAndEventId(this.userId, this.event.id, this.notificationSetting)
      .subscribe(response => {
          this.savingChanges = false;
          this.toast.success('Notification settings for this event successfully updated');
        }, error => {
        this.savingChanges = false;
        this.error = true;
        }
      );
  }

}
