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

  constructor(private formBuilder: FormBuilder,
              private notificationSettingsService: NotificationSettingsService,
              private toast: ToastService) { }

  ngOnInit() {
    const eventStartDate = new Date(this.event.timeLineStart);
    this.max = new Date(eventStartDate.getTime());
    const maxNotificationDate = eventStartDate.getDate();
    this.max.setDate(maxNotificationDate);

    this.form = this.formBuilder.group({
      emailNotif: [this.notificationSetting.emailNotificationOn, [Validators.required]],
      startDate: [this.notificationSetting.startDate, []],
      period: [this.notificationSetting.period, [Validators.required, Validators.min(0)]],
      countDownOn: [this.notificationSetting.countDownOn, []]
      }, {validator: maxPeriod(this.max, 'startDate', 'period')}
    );
  }

  fixDate(){
    this.notificationSetting.startDate = new Date(this.notificationSetting.startDate.getTime()
      + Math.abs(this.notificationSetting.startDate.getTimezoneOffset()*60000));
  }

  save() {
    this.error = false;
    this.savingChanges = true;

    if(this.form.get('startDate').value){
      this.fixDate();
    }

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
