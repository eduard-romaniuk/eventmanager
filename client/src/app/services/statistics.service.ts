import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Statistics} from "../model/statistics";

@Injectable()
export class StatisticsService {
  private base_url = '/statistics';
  
  constructor(private http: HttpClient) {
  }
  
  public loadStatistics(): Observable<Statistics> {
      return this.http.get<Statistics>(this.base_url + "/", );
  }
  
}