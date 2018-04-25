import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';

import { User } from '../model/user.model';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class UserService {

  private userUrl = 'http://localhost:8081/users';

  headers: HttpHeaders;

  constructor(private http:HttpClient) {}

  public getUsers() {
    return this.http.get(this.userUrl);
  }

  public getUserById(id) {
    return this.http.get(this.userUrl+ "/"+ id);
  }

  public createUser(user) {
    return this.http.post(this.userUrl, user);
  }

  public updateUser(newUser : User) {
    console.log("newUser in updateUser" + newUser);
    const url = `${this.userUrl}/${newUser.id}`;

    this.headers = new HttpHeaders(newUser? {newUser: JSON.stringify({
      id: newUser.id,
      login: newUser.login,
      password: newUser.password,
      name: newUser.name,
      surName: newUser.surName,
      email: newUser.email,
      birth: newUser.birth,
      phone: newUser.phone,
      sex: newUser.sex,
      active: newUser.active,
      image: newUser.image,
      regDate: newUser.regDate,
      friends: newUser.friends,
      wishList: newUser.wishList,
      events: newUser.events,
      settings: newUser.settings,
      confLink: newUser.confLink
    }) } : {});

    console.log(`Update User(${this.headers.get("newUser")})`);

    return this.http.put(url, newUser, httpOptions);
  }

  public deleteUser(user) {
    return this.http.delete(this.userUrl + "/"+ user.id);
  }

}
