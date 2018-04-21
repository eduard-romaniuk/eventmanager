import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class AuthService {

  authenticated = false;
  headers: HttpHeaders;
  username: string;

  constructor(private http: HttpClient) {
  }

  authenticate(credentials, callback) {

        console.log(`try to login(${credentials.username} ${credentials.password})`);

        this.headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
        } : {});

        this.http.get('http://localhost:8080/user', {headers: this.headers}).subscribe(response => {
          if (response['name']) {
              this.authenticated = true;
              this.username = credentials.username;
          } else {
              this.authenticated = false;
          }
          return callback && callback();
        });
  }

  logout(callback) {
    this.authenticated = false;
    this.headers = null;
    return callback && callback();
  }

}