/**
 * Created by Shvets on 04.05.2018.
 */
import {User} from "./user";
import {Note} from "./note";
export class Folder {
  id: number;
  name: string;
  creator: User;
  users: User[];
  notes: Note[];
}
