import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {User} from '../model/user';
import {Observable} from 'rxjs/Observable';
import {Event} from "../model/event";

@Injectable()
export class UserService {
  private base_url = '/users';

  headers: HttpHeaders;

  constructor(private http: HttpClient) {
  }

  public getUsers() {
    return this.http.get(this.base_url + "/all");
  }

  getUsersPagination(limit: number, offset: number)
  :Observable<HttpResponse<User[]>> {
    const params = new HttpParams()
      .set('limit', limit.toString())
      .set('offset', offset.toString());
    return this.http.get<User[]>(this.base_url + "/all",{params: params, observe: 'response'});
  }

  public getUserById(id) {
    return this.http.get(this.base_url + "/" + id);
  }

  public createUser(user) {
    return this.http.post(this.base_url + "/", user);
  }

  public updateUser(newUser: User): Observable<User> {
    return this.http.put<User>(`${this.base_url}/${newUser.id}`, newUser);
  }

  public deleteUser(user) {
    return this.http.delete(this.base_url + "/" + user.id);
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

  searchByLoginOrByNameAndSurname(queryString) {
    const params = new HttpParams().set('query', queryString);
    console.log("Search query - " + queryString);
    return this.http.get(this.base_url + "/search",{params: params});
  }

  searchByLoginOrByNameAndSurnamePagination(queryString: string, limit: number, offset: number)
  :Observable<HttpResponse<User[]>> {
    const params = new HttpParams()
      .set('query', queryString)
      .set('limit', limit.toString())
      .set('offset', offset.toString());
    console.log("Search query in pag - " + queryString);
    return this.http.get<User[]>(this.base_url + "/search",{params: params, observe: 'response'});
  }

  public getEventsByUserId(id, isPrivate, isSent): Observable<Event[]> {
    return this.http.get<Event[]>(this.base_url + "/" + id + "/events", {
      params: {
        isPrivate: isPrivate,
        isSent: isSent
      }
    });
  }

  public getCurrentUserEvents(id): Observable<Event[]> {
    return this.http.get<Event[]>(this.base_url + "/" + id + "/myevents", );
  }

  updateUserPassword(id, oldPassword, newPassword) {
    console.log("user id in updateUserPassword - " + id);
    const params = new HttpParams()
      .set('oldPassword', oldPassword)
      .set('newPassword', newPassword);
    return this.http.put(this.base_url + "/" + id + "/changePassword", {}, {params: params});
  }

  updateUserEmail(id, oldEmail, newEmail) {
    console.log("user id in updateUserEmail - " + id);
    const params = new HttpParams()
      .set('oldEmail', oldEmail)
      .set('newEmail', newEmail);
    return this.http.put(this.base_url + "/" + id + "/changeEmail", {}, {params: params});
  }

  // Friends functionality

  public addFriendRequest(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.post(this.base_url + "/addFriendRequest",
      {}, {params: params});
  }

  public acceptFriendRequest(requester, accepter) {
    const params = new HttpParams()
      .set('requester', requester)
      .set('accepter', accepter)
      .set('accept', "true");
    return this.http.put(this.base_url + "/answerFriendRequest",
      {}, {params: params});
  }

  public declineFriendRequest(requester, accepter) {
    const params = new HttpParams()
      .set('requester', requester)
      .set('accepter', accepter)
      .set('accept', "false");
    return this.http.put(this.base_url + "/answerFriendRequest",
      {}, {params: params});
  }

  public deleteRelationship(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.delete(this.base_url + "/deleteRelationship", {params: params});
  }

  public getRelationshipStatus(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.get(this.base_url + "/getRelationshipStatus",
      {params: params, responseType: 'text'});
  }

  public getRelationshipStatusId(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.get(this.base_url + "/getRelationshipStatusId",
      {params: params, responseType: 'text'});
  }

  public getRelationshipStatusAndActionUserId(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.get<Map<string, any>>(this.base_url + "/getRelationshipStatusAndActiveUserId",
      {params: params});
  }

  public getRelationshipStatusIdAndActionUserId(from, to) {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.get<Map<string, any>>(this.base_url + "/getRelationshipStatusIdAndActiveUserId",
      {params: params});
  }

  public getOutcomingRequests(id) {
    return this.http.get<User[]>(this.base_url + "/" + id + "/outcomingRequests");
  }

  public getIncomingRequests(id) {
    return this.http.get<User[]>(this.base_url + "/" + id + "/incomingRequests");
  }

  public getFriends(id) {
    return this.http.get<User[]>(this.base_url + "/" + id + "/friends");
  }

}
