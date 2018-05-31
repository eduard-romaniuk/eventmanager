import {User} from "./user";
import {Category} from "./category";
export class Event {
  id: number;
  creator: User;
  name: string;
  description: string;
  place: string;
  timeLineStart: any;
  timeLineFinish: any;
  period: number;
  image: any;
  isSent: boolean;
  isPrivate: boolean;
  participants: any[];
  category: Category;
}
