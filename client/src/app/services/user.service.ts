import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs/Observable';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class UserService {
  private base_url = '/users';

  headers: HttpHeaders;

  constructor(private http:HttpClient) {}

  public getUsers() {
    return this.http.get(this.base_url + "/all");
  }

  public getUserById(id) {
    return this.http.get(this.base_url + "/"+ id);
  }

  public createUser(user) {
    return this.http.post(this.base_url, user);
  }

  //TODO Do update without headers
  // updateUser(newUser: User, callback?, errorCallback?) {
  //   console.log("newUser in updateUser" + newUser);
  //   const url = `${this.base_url}/${newUser.id}`;
  //   this.http.patch(url, newUser).subscribe(
  //     response => {
  //       return callback && callback();
  //     },
  //     error => {
  //       return errorCallback && errorCallback();
  //     })
  // }

  public updateUser(newUser : User) {
    console.log("newUser in updateUser" + newUser);
    const url = `${this.base_url}/${newUser.id}`;

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
      verified: newUser.verified,
      image: newUser.image,
      regDate: newUser.regDate,
      friends: newUser.friends,
      wishList: newUser.wishList,
      events: newUser.events,
      settings: newUser.settings,
      token: newUser.token
    }) } : {});

    console.log(`Update User(${this.headers.get("newUser")})`);

    return this.http.put(url, newUser, httpOptions);
  }

  public deleteUser(user) {
    return this.http.delete(this.base_url + "/"+ user.id);
  }

  isEmailExists(email: String): Observable<boolean> {
    return this.http.get<boolean>(this.base_url + '/exists/email/' + email);
  }

  isUsernameExists(username: String): Observable<boolean> {
    return this.http.get<boolean>(this.base_url + '/exists/username/' + username);
  }

  getUser(username: String): Observable<User> {
    return this.http.get<User>(this.base_url + '/by-username/' + username);
  }
}
