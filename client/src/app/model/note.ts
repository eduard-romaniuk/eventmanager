import {User} from "./user";
import {Folder} from "./folder";
export class Note {
  id: number;
  creator: User;
  name: string;
  description: string;
  image: any;//TODO image
  folder: Folder;
}
