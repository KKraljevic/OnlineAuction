import { Component, OnInit, Input } from '@angular/core';
import { Bid } from 'src/app/Model/bid';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { User } from 'src/app/Model/user';
import { UserService } from 'src/app/Services/user-service.service';

@Component({
  selector: 'app-bids',
  templateUrl: './bids.component.html',
  styleUrls: ['./bids.component.css', '../user-profile/user-profile.component.css']
})
export class BidsComponent implements OnInit {

  bids: Bid[] = [];
  hasBids: boolean;
  currentUser: User;
  show: boolean = false;

  totalPages:number;
  currentPage:number =0;

  sumDiff: number;
  hourDiff: number;
  dayDiff: number;
  weekDiff: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService) {
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;

      this.userService.getBids(this.currentUser.id).subscribe(b => {
        this.bids = b['content'];
        this.totalPages=b['totalPages'];
        this.currentPage=b['number']
        if (this.bids.length===0)
          this.hasBids = false;
        else {
          this.hasBids = true;
        }
        this.show=true;
      },
      error => this.show=true);
    });

  }

  getTimeLeft(bidDate: Date): string {

    var date = new Date(bidDate.toString());
    this.sumDiff = (date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);

    if (this.sumDiff < 1) {
      this.hourDiff = Math.round(this.sumDiff * 24);
      return this.hourDiff + "h";
    }
    if (this.sumDiff < 7) {
      this.dayDiff = Math.round(this.sumDiff);
      this.hourDiff = Math.round((this.sumDiff - this.dayDiff) * 24);
      return this.dayDiff + " days, " + this.hourDiff + "h";
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

  loadBids(page: number){
    this.userService.getBids(this.currentUser.id,page).subscribe(
      data =>{
        this.bids=data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];      
      });
  }

}
