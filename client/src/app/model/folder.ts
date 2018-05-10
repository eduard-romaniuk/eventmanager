/**
 * Created by Shvets on 04.05.2018.
 */
import {Event} from "./event";
import {User} from "./user";
import {Note} from "./note";
export class Folder {
  id: number;
  name: string;
  events: Event[];
  creator: User;
  users: User[];
  notes: Note[];
}
