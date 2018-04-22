import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class AuthService {

  authenticated: boolean;
  username: string;

  constructor(private http: HttpClient) {
    const authToken = sessionStorage.getItem('authToken');

    console.log(authToken);

    if (authToken === null || authToken === '') {
      this.authenticated = false;
      sessionStorage.setItem('authToken', '');
    } else {
      this.authenticated = true;
    }
  }

  authenticate(credentials, callback?, errorCallback?) {

    sessionStorage.setItem('authToken', 'Basic ' + btoa(credentials.username + ':' + credentials.password));

    this.http.get('/user').subscribe(response => {
      if (response['name']) {
          this.authenticated = true;
          this.username = credentials.username;
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

  logout(callback?) {
    this.authenticated = false;
    sessionStorage.setItem('authToken', '');
    return callback && callback();
  }

  isLoggedIn() {
    return this.authenticated;
  }

}