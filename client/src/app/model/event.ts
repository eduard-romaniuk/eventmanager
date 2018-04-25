export class Event {
  id: number;
  creator: any; //TODO user
  name: string;
  description: string;
  place: string;
  timeLineStart: any;//TODO date & time
  timeLineFinish: any;//TODO date & time
  period: number; //
  image: any;//TODO image
  isSent: boolean;
  isPrivate: boolean;
  participants: any[]; //TODO User[]
}
