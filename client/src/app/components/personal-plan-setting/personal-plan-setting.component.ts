import {Component, OnInit} from '@angular/core';

import {PersonalPlanSetting} from '../../model/personalPlanSetting';
import {PersonalPanSettingService} from '../../services/personal-pan-setting.service';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {dateValidator} from "../../utils/validation-tools";

@Component({
  selector: 'app-personal-plan-notification-setting',
  templateUrl: './personal-plan-setting.component.html',
  styleUrls: ['./personal-plan-setting.component.css']
})
export class PersonalPlanSettingComponent implements OnInit {

  setting: PersonalPlanSetting;
  form: FormGroup;

  constructor(private router: Router,
              private settingService: PersonalPanSettingService,
              private formBuilder: FormBuilder) {

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
      timeLineStartControl: ['', [Validators.required, dateValidator()]],
      periodControl: ['', [Validators.required, Validators.min(0)]],
    });
  }

  public update() {
    console.log(this.setting.sendPlan);
    this.settingService.update(this.setting).subscribe(response => {
      this.router.navigate(['home']);
    }, error => console.error(error));
    console.log("Personal plan setting was update")
  }
}
