import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs/Observable';
import { Event } from "../model/event";

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
    return this.http.post(this.base_url + "/", user);
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

  public updateUser(newUser : User): Observable<User> {
    return this.http.put<User>(`${this.base_url}/${newUser.id}`, newUser);
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

  searchUserByLogin(login) {
    const params = new HttpParams().set('login', login);
    console.log("Login in searchUserByLogin - " + login);
    return this.http.get(this.base_url + "/search",{params: params});
  }

  public getEventsByUserId(id): Observable<Event[]> {
    return this.http.get<Event[]>(this.base_url + "/"+ id + "/events");
  }

  updateUserPassword(newUser: User) {
    console.log("user in updateUserPassword - " + newUser);
    return this.http.put(this.base_url + "/changePassword", newUser)
  }
}
