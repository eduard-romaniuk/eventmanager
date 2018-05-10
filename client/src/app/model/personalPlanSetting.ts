import {User} from "./user";

export class PersonalPlanSetting {
  user: User;
  sendPlan: boolean;
  fromDate: Date;
  planPeriod: number;
  notificationPeriod: number;

}
