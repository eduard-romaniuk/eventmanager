import {Component, OnInit} from '@angular/core';

import {PersonalPlanSetting} from '../../model/personalPlanSetting';
import {PersonalPanSettingService} from '../../services/personal-pan-setting.service';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-personal-plan-notification-setting',
  templateUrl: './personal-plan-setting.component.html',
  styleUrls: ['./personal-plan-setting.component.css']
})
export class PersonalPlanSettingComponent implements OnInit {

  setting:PersonalPlanSetting;


  constructor(private router: Router, private settingService: PersonalPanSettingService) {

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
      }

  public update(){
    this.settingService.update(this.setting).subscribe(response => {
      this.router.navigate(['home']);
    }, error => console.error(error));
    console.log("Personal plan setting was update")
  }
}
