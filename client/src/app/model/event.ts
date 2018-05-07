import {User} from "./user";
import {Folder} from "./folder";
export class Event {
  id: number;
  creator: User;
  name: string;
  description: string;
  place: string;
  folder: Folder;
  timeLineStart: any;//TODO date & time
  timeLineFinish: any;//TODO date & time
  period: number; //
  image: any;//TODO image
  isSent: boolean;
  isPrivate: boolean;
  participants: any[]; //TODO User[]
}
