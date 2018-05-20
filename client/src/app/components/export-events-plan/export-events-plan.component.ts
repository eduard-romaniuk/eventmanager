import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";

import {saveAs} from 'file-saver/FileSaver';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-export-events-plan',
  templateUrl: './export-events-plan.component.html',
  styleUrls: ['./export-events-plan.component.css']
})
export class ExportEventsPlanComponent {

  private url = '/event';

  form: FormGroup;


  today: Date = new Date();
  date_range = [
    this.today,
    new Date(this.today.getFullYear(), this.today.getMonth() + 1, 0, 23, 59)
  ];

  min = new Date();
  max = new Date(2049,11,31);


  constructor(private auth: AuthService,
              private http: HttpClient,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {

    this.form = this.formBuilder.group({

      dateRange:['', [ Validators.required]],
    });

  }



  download() {

    console.log("download");
    this.http.get(this.url +"/downloadPlan", {
        params: {from: this.date_range[0].toISOString(), to: this.date_range[1].toISOString()},
        responseType: "blob"
    }
    ).subscribe(
      (response) => {
        let mediaType = 'application/pdf';
        let blob = new Blob([response], {type: mediaType});
        let filename = 'event-plan.pdf';
        saveAs(blob, filename);
      });

    console.log(this.url,this.date_range[0].toISOString(),this.date_range[1].toISOString());

  }

  send() {
    console.log("GET");
    this.http.get(this.url+"/sendPlan", {
      params: {from: this.date_range[0].toISOString(), to: this.date_range[1].toISOString()}
    }).subscribe();

    console.log(this.url,this.date_range[0].toISOString(),this.date_range[1].toISOString());
  }

}
