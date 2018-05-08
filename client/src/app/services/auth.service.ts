import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../model/user';
import {UserService} from './user.service';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthService {

  authenticated: boolean;
  login: string;
  base_url = '/users';
  current_user: Observable<User>;

  constructor(private http: HttpClient, private users: UserService) {
    const authToken = sessionStorage.getItem('authToken');
    if (authToken === null || authToken === '') {
      this.authenticated = false;
      sessionStorage.setItem('authToken', '');
    } else {
      this.authenticated = true;
      this.current_user = this.users.getUser(sessionStorage.getItem('login'));
    }
  }

  authenticate(credentials, callback?, errorCallback?) {
    this.setSessionAuthToken(credentials.login, credentials.password);

    this.http.get(this.base_url + '/').subscribe(response => {
        if (response['name']) {
          this.authenticated = true;
          this.setSessionLogin(credentials.login);
          this.current_user = this.users.getUser(credentials.login);
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

  registration(user: User) {
    return this.http.post(this.base_url + '/', user)
  }

  emailVerification(str: String) {
    alert('it\'s work');
  }

  logout(callback?) {
    this.authenticated = false;
    sessionStorage.setItem('authToken', '');
    sessionStorage.setItem('login', '');
    return callback && callback();
  }

  getUser() {
    return this.current_user;
  }

  setSessionAuthToken(login: String, password: String){
    sessionStorage.setItem('authToken', 'Basic ' + btoa(login + ':' + password));
  }

  setSessionLogin(login: String){
    sessionStorage.setItem('login', login.toString());
  }

}
