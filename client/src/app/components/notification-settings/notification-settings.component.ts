import {Component, Input, OnInit} from '@angular/core';
import {Event} from "../../model/event";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationSettings} from "../../model/notificationSettings";
import {NotificationSettingsService} from "../../services/notification-settings.service";
import {ToastService} from "../../services/toast.service";
import {maxPeriod} from "../../utils/validation-tools";

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
  max = new Date();
  //maxPeriod: number;

  constructor(private formBuilder: FormBuilder,
              private notificationSettingsService: NotificationSettingsService,
              private toast: ToastService) { }

  ngOnInit() {
    const eventStartDate = new Date(this.event.timeLineStart);
    const maxNotificationDate = eventStartDate.getDate() - 1;
    this.max.setDate(maxNotificationDate);

    // if(this.notificationSetting.startDate){
    //   const notificationStartDate = new Date(this.notificationSetting.startDate);
    //   console.log("notificationStartDate - " + notificationStartDate);
    //   this.maxPeriod = eventStartDate.valueOf() - notificationStartDate.valueOf();
    // } else {
    //   this.maxPeriod = 0;
    // }
    //
    // console.log("this.maxPeriod - " + this.maxPeriod);

    this.form = this.formBuilder.group({
      emailNotif: [this.notificationSetting.emailNotificationOn, [Validators.required]],
      startDate: [this.notificationSetting.startDate, []],
      period: [this.notificationSetting.period, [Validators.required, Validators.min(0)]],
      countDownOn: [this.notificationSetting.countDownOn, []]
      }, {validator: maxPeriod(eventStartDate, 'startDate', 'period')}
    );
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
