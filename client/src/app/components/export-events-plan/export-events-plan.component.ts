import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {User} from "../../model/user";
import {saveAs} from 'file-saver/FileSaver';

@Component({
  selector: 'app-export-events-plan',
  templateUrl: './export-events-plan.component.html',
  styleUrls: ['./export-events-plan.component.css']
})
export class ExportEventsPlanComponent {

  private url = '/event';
  public toDate: string;
  public fromDate: string;
  constructor(private auth: AuthService, private http: HttpClient) {
  }

  download() {
    console.log("GET");
    this.http.get(this.url +"/downloadPlan", {
        params: {from: this.fromDate, to: this.toDate},
        responseType: "blob"
    }
    ).subscribe(
      (response) => {
        var mediaType = 'application/pdf';
        var blob = new Blob([response], {type: mediaType});
        var filename = 'event-plan.pdf';
        saveAs(blob, filename);
      });

    console.log(this.url, this.fromDate, this.toDate);

  }

  send() {
    console.log("GET");
    this.http.get(this.url+"/sendPlan", {
        params: {from: this.fromDate, to: this.toDate}}
    ).subscribe();

    console.log(this.url, this.fromDate, this.toDate);
  }



}
