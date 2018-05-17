/**
 * Created by Shvets on 04.05.2018.
 */
import {User} from "./user";
import {Note} from "./note";
import {Member} from "./member";
export class Folder {
  id: number;
  name: string;
  creator: User;
  members: Member[];
  notes: Note[];
}
