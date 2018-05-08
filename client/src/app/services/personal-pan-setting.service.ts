import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {PersonalPlanSetting} from '../model/personalPlanSetting';
import {Observable} from "rxjs/Observable";


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'})
};

@Injectable()
export class PersonalPanSettingService {

  private base_url = '/users/';

  constructor(private http: HttpClient){

  }

  public get(): Observable<PersonalPlanSetting> {

    return this.http.get<PersonalPlanSetting>( this.base_url+"plan/");
  }

  public update(setting){

    return this.http.post(this.base_url+"plan/", setting);
  }

}

