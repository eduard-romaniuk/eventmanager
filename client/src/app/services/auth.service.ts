import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { UserService } from './user.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthService {

  authenticated: boolean;
  username: string;
  base_url = '/users';
  current_user: Observable<User>;

  constructor(private http: HttpClient, private users: UserService) {
    const authToken = sessionStorage.getItem('authToken');
    if (authToken === null || authToken === '') {
      this.authenticated = false;
      sessionStorage.setItem('authToken', '');
    } else {
      this.authenticated = true;
      this.current_user = this.users.getUser(sessionStorage.getItem('username'));
    }
  }

  authenticate(credentials, callback?, errorCallback?) {

    sessionStorage.setItem('authToken', 'Basic ' + btoa(credentials.username + ':' + credentials.password));

    this.http.get(this.base_url + '/').subscribe(response => {
      if (response['name']) {
          this.authenticated = true;
          sessionStorage.setItem('username', credentials.username);
          this.current_user = this.users.getUser(credentials.username);
      } else {
          this.logout();
      }
      return callback && callback();
    },
    error => {
      this.logout();
      return errorCallback && errorCallback();
    });
  }

  registration(user: User, callback?, errorCallback?) {
    this.http.post(this.base_url + '/', user).subscribe(
      response => {
        return callback && callback();
      },
      error => {
        return errorCallback && errorCallback();
      })
  }

  emailVerification(str: String) {
    alert('it\'s work');
  }

  logout(callback?) {
    this.authenticated = false;
    sessionStorage.setItem('authToken', '');
    sessionStorage.setItem('username', '');
    return callback && callback();
  }

}
