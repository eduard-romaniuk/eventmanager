import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";

import {saveAs} from 'file-saver/FileSaver';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ToastService} from "../../services/toast.service";
import {Router} from "@angular/router";

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

  min = new Date(this.today.getFullYear(),this.today.getMonth(),this.today.getDate(),0,0);
  max = new Date(2049,11,31);


  constructor(private auth: AuthService,
              private http: HttpClient,
              private formBuilder: FormBuilder,
              private toast: ToastService,
              private router: Router) {
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
        this.router.navigate(['home']);
        this.toast.info('Choose a path to save the events plan');
      },error => {
        this.toast.error('You do not have any events for the selected period');
        console.error(error)});

  }

  send() {
    console.log("GET");
    this.http.get(this.url+"/sendPlan", {
      params: {from: this.date_range[0].toISOString(), to: this.date_range[1].toISOString()}
    }).subscribe((response) => {
      this.router.navigate(['home']);
      this.toast.success('Events plan was sending to your email ');
    },error => {
      this.toast.error('You do not have any events for the selected period');
      console.error(error)});
  }

}
