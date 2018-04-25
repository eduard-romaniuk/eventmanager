import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UsersService {
  base_url = '/users';

  constructor(private http: HttpClient) {}

  isEmailExists(email: String): Observable<boolean> {
    return this.http.get<boolean>(this.base_url + '/exists/email/' + email);
  }

  isUsernameExists(username: String): Observable<boolean> {
    return this.http.get<boolean>(this.base_url + '/exists/username/' + username);
  }

  getUser(username: String): Observable<User> {
  	return this.http.get<User>(this.base_url + '/' + username);
  }

}