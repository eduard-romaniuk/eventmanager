import {User} from './user';
import {Tag} from "./tag";
import {WishList} from "./wishlist";

export class Item {
  id: number;
  name: String;
  description: String;
  priority: number;
  wishList: WishList;
  images: String[];
  booker: User;
  likes: number;
  tags: Tag[];
  likesCount: number;

  //TODO: duedate, links
}
