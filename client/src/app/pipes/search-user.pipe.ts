import { Pipe, PipeTransform } from '@angular/core';
import {User} from "../model/user";

@Pipe({
  name: 'searchUser'
})

export class SearchUserPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if(!items) return [];
    if(!searchText) return items;
    searchText = searchText.toLowerCase();
    return items.filter( (user: User) => {
      return user.login.toLowerCase().includes(searchText);
    });
  }
}
