
import {Tag} from "./tag";
import {Booker} from "./booker";

export class Item {
  id: number;
  name: String;
  description: String;
  priority: number;
  wishListId: number;
  images: String[] = [];
  bookers: Booker[];
  likes: number;
  tags: Tag[];
  hasLiked: boolean;

  //TODO: duedate, links
}
