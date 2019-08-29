import { Component, OnInit, Input } from '@angular/core';
import { Bid } from 'src/app/Model/bid';
import { User } from 'src/app/Model/user';

@Component({
  selector: 'app-bids-table',
  templateUrl: './bids-table.component.html',
  styleUrls: ['./bids-table.component.css']
})
export class BidsTableComponent implements OnInit {

  @Input() bids: Bid[] = [];
  @Input() activeTab: number;

  sumDiff: number;
  hourDiff: number;
  dayDiff: number;
  weekDiff: number;

  constructor() { }

  ngOnInit() {
  }

  getTimeLeft(bidDate: Date): string {

    var date = new Date(bidDate.toString());
    this.sumDiff = (date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);

    if (this.sumDiff < 1) {
      this.hourDiff = Math.round(this.sumDiff * 24);
      return Math.abs(this.hourDiff) + "h";
    }
    if (this.sumDiff < 7) {
      this.dayDiff = Math.floor(this.sumDiff);
      this.hourDiff = Math.round((this.sumDiff - this.dayDiff) * 24);
      return this.dayDiff + " days, " + Math.abs(this.hourDiff) + "h";
    }
    else {
      this.weekDiff = Math.floor(this.sumDiff / 7);
      this.dayDiff = Math.round(this.sumDiff - this.weekDiff * 7);
      if (this.dayDiff > 0)
        return this.weekDiff + " weeks, " + this.dayDiff + " days";
      else {
        this.hourDiff = Math.round((this.sumDiff - this.weekDiff * 7) * 24);
        return this.weekDiff + " weeks, " + Math.abs(this.hourDiff) + "h";
      }
    }
  }

}
