import {User} from './user';

export class Item {
  id: number;
  name: String;
  descriprion: String;
  priority: number;
  wishList: any;
  images: String[];
  booker: User;
  likes: number;
  tags: String[];

  //TODO: duedate, links
}
