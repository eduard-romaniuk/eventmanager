import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {Message} from "../model/message";

@Injectable()
export class MessageService {
  
  constructor(private router: Router,private http: HttpClient) {
  }
  
  public loadMessages(): Observable<Message[]> {
      return this.http.get<Message[]>(this.router.url, );
  }
  
}
