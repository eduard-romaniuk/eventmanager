import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";

import {saveAs} from 'file-saver/FileSaver';
import {dateLessThan } from "../../utils/validation-tools";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-export-events-plan',
  templateUrl: './export-events-plan.component.html',
  styleUrls: ['./export-events-plan.component.css']
})
export class ExportEventsPlanComponent {

  private url = '/event';
  public toDate: string;
  public fromDate: string;
  form: FormGroup;

  constructor(private auth: AuthService,
              private http: HttpClient,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {

    this.form = this.formBuilder.group({

      timeLineStartControl:['', [ Validators.required]],
      timeLineFinishControl: ['', [ Validators.required]],

    },{ validator:dateLessThan('timeLineStartControl', 'timeLineFinishControl'), });

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

    console.log(this.url, this.fromDate, this.toDate,this);

  }

  send() {
    console.log("GET");
    this.http.get(this.url+"/sendPlan", {
        params: {from: this.fromDate, to: this.toDate}}
    ).subscribe();

    console.log(this.url, this.fromDate, this.toDate);
  }



}
