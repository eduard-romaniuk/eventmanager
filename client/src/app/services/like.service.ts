import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {AuthService} from "./auth.service";

import {Observable} from "rxjs/Observable";
import {Item} from "../model/item";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
};

@Injectable()
export class LikeService {

  headers: HttpHeaders;
  private base_url = '/item';
  private userId: number;

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) {

    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;
    });

  }

  public wasLiked (itemId: number): Observable<boolean>{
    return this.http.get<boolean>(this.base_url + "/likes" + "/" + this.userId + "/" + itemId);
  }

  public addLike (item: Item): void {
    if (item.hasLiked == false){
      item.likes++;
      item.hasLiked = true;

      console.log("Like!");

      this.http.post(this.base_url + "/like/" + item.id, this.userId).subscribe();

    } else {
      item.likes--;
      item.hasLiked = false;

      console.log("Dislike!");

      this.http.post(this.base_url + "/dislike/" + item.id, this.userId).subscribe();
    }
  }



}
