import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {NotificationSettings} from "../model/notificationSettings";

@Injectable()
export class NotificationSettingsService {

  private base_url = '/notificationSettings';

  constructor(private http: HttpClient) { }

  getByUserIdAndEventId(userId: number, eventId: number) {
    console.log("in getByUserIdAndEventId userId - " + userId + " and event id - " + eventId);
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('eventId', eventId.toString());
    return this.http.get(this.base_url, {params: params});
  }

  updateByUserIdAndEventId(userId: number, eventId: number, notificationSetting: NotificationSettings) {
    console.log("in updateByUserIdAndEventId userId - " + userId + " and event id - " + eventId);
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('eventId', eventId.toString());
    return this.http.post(this.base_url, notificationSetting, {params: params});
  }

}
