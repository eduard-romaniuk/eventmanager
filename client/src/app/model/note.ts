import {User} from "./user";
import {Folder} from "./folder";
export class Note {
  id: number;
  creator: User;
  name: string;
  description: string;
  place: string;
  period: number; //
  image: any;//TODO image
  isSent: boolean;
  isPrivate: boolean;
  folder: Folder;
}
