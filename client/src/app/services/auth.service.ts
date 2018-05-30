import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../model/user';
import {UserService} from './user.service';
import {ToastService} from './toast.service';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthService {

  authenticated: boolean;
  login: string;
  base_url = '/auth';
  current_user: Observable<User>;
  SESSION_STORAGE_PREFIX = 'Basic ';

  constructor(private http: HttpClient, private users: UserService, private toast: ToastService) {
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
          const user = this.users.getUser(credentials.login);
          user.subscribe(response => {
            if(!response.verified) {
              this.logout();
              this.toast.error('Your account not verified!');
            } else {
              this.authenticated = true;
              this.setSessionLogin(credentials.login);
              this.current_user = user;
            }
          })
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
    return this.users.createUser(user);
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
    sessionStorage.setItem('authToken', this.SESSION_STORAGE_PREFIX + btoa(login + ':' + password));
  }

  setSessionLogin(login: String){
    sessionStorage.setItem('login', login.toString());
  }

  getEncodedLoginAndPassword(){
    const authToken = sessionStorage.getItem('authToken');
    return authToken.split(this.SESSION_STORAGE_PREFIX).pop();
  }

  getDecodedLoginAndPassword(){
    return atob(this.getEncodedLoginAndPassword());
  }

  getSessionLogin(){
    const loginAndPassword = this.getDecodedLoginAndPassword();
    return loginAndPassword.substr(0, loginAndPassword.indexOf(":"));
  }

  getSessionPassword(){
    return this.getDecodedLoginAndPassword().split(":").pop();
  }

  recoverPassword(login: string) {
    return this.http.post(this.base_url + '/recover/password/' + login, '');
  }

  recoverLogin(email: string) {
    return this.http.post(this.base_url + '/recover/login/' + email, '');
  }

  updateUserPassword(login: string, token: string, pass: string) {
    return this.http.put(`${this.base_url}/changePassword/${login}/${token}`, pass)
  }
}
