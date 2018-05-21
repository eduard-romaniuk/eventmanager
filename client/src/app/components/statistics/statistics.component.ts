import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../services/statistics.service';
import {Statistics} from "../../model/statistics";

@Component({
  selector: 'app-chat',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})

export class StatisticsComponent{
  statistics: Statistics;
  
  constructor(private statService: StatisticsService, private router: Router){
  }
  
  ngOnInit() {
    
    this.statService.loadStatistics().subscribe((statistics: any) => {
      this.statistics = statistics;
    });
  }
  
  public goHome(){
     this.router.navigate(['home']);
  }
}