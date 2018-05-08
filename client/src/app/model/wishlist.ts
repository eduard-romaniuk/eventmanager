import {User} from "./user";
import {Item} from "./item";

export class WishList {
  id: number;
  userId: number;
  name: String;
  items: Item[];
}
