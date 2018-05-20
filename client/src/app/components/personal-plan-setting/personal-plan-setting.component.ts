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

  min = new Date();
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
      periodControl: ['', [Validators.required, Validators.min(0)]],
    });
  }

  public update() {
    console.log(this.setting.sendPlan);
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
    this.update();
    this.toast.success('Sending Personal Plan was enable');

  }

  public disable(){

    this.setting.sendPlan = false;
    this.update();
    this.toast.warn('Sending Personal Plan was disable');

  }
}
