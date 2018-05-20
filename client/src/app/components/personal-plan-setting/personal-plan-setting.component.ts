import {Component, OnInit} from '@angular/core';

import {PersonalPlanSetting} from '../../model/personalPlanSetting';
import {PersonalPanSettingService} from '../../services/personal-pan-setting.service';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastService} from "../../services/toast.service";


@Component({
  selector: 'app-personal-plan-notification-setting',
  templateUrl: './personal-plan-setting.component.html',
  styleUrls: ['./personal-plan-setting.component.css']
})
export class PersonalPlanSettingComponent implements OnInit {

  setting: PersonalPlanSetting;
  form: FormGroup;

  today: Date = new Date();
  min = new Date(this.today.getFullYear(),this.today.getMonth(),this.today.getDate(),0,0);
  max = new Date(2049,11,31);

  constructor(private router: Router,
              private settingService: PersonalPanSettingService,
              private formBuilder: FormBuilder,
              private toast: ToastService) {

  }

  ngOnInit() {

    this.settingService.get().subscribe((setting: any) => {
      if (setting) {
        console.log(setting);
        this.setting = setting;
        console.log(this.setting);
      } else {
        console.log(`Personal plan setting not found!`);
      }
    });

    this.form = this.formBuilder.group({
      timeLineStartControl: ['', [Validators.required]],
      planPerioddControl: ['', [Validators.required, Validators.min(0)]],
      notificationPeriodControl: ['', [Validators.required, Validators.min(0)]]
    });
  }

  public update() {

    this.setting.fromDate= new Date(this.setting.fromDate.getFullYear(),this.setting.fromDate.getMonth(),this.setting.fromDate.getDate()+1);
    console.log(this.setting);
    this.settingService.update(this.setting).subscribe(response => {
      this.router.navigate(['home']);
      this.toast.success('Personal Plan Setting was successfully updating');
    }, error =>{
      this.toast.warn('An error occurred while updating the data, please try again');
      console.error(error)});

    console.log("Personal plan setting was update")
  }

  public enable(){

    this.setting.sendPlan = true;
    this.setting.fromDate = new Date(this.setting.fromDate);
    this.update();
    this.toast.success('Sending Personal Plan was enable');

  }

  public disable(){

    this.setting.sendPlan = false;
    this.setting.fromDate = new Date(this.setting.fromDate);
    this.update();
    this.toast.warn('Sending Personal Plan was disable');

  }
}
