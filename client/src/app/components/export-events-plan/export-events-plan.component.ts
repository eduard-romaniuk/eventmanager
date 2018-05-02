import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {User} from "../../model/user";

@Component({
  selector: 'app-export-events-plan',
  templateUrl: './export-events-plan.component.html',
  styleUrls: ['./export-events-plan.component.css']
})
export class ExportEventsPlanComponent {

  public url:string = "http://localhost:8080/event/downloadPlan";

  public toDate: string;
  public fromDate: string;
  private user: User;
  private id:number;

  constructor(private auth : AuthService, private http: HttpClient) { }


  download(){
    console.log("POST");
    this.auth.current_user.subscribe((data: any) => {this.user = data});
    this.id=this.user.id;
    this.http.post(this.url, {userId:this.id,from:this,to:this});
    console.log(this.url,this.fromDate,this.toDate,this.id)

  }

  send(){

  }


}
