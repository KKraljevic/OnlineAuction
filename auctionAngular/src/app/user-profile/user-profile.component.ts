import { Component, OnInit, Input, DefaultIterableDiffer } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../authentication.service';
import { Bid } from '../bid';
import { UserService } from '../user-service.service';
import { Item } from '../item';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  currentUser: User;
  bids: Bid[] = [];
  items: Item[] = [];
  id: number;
  showInfoBids: boolean;

  sumDiff: number;
  hourDiff: number;
  dayDiff: number;
  weekDiff: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService, private router: Router, private route: ActivatedRoute) {
    this.currentUser = new User();
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
    });
    this.userService.getBids(this.currentUser.id).toPromise().then( b =>
      {
      this.bids=b;
      if(b!=null)
        this.showInfoBids=false;
      else
        this.showInfoBids=true;

      });
  }

  ngOnInit(): void {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      this.items = x.items;
    });
    
  }

  getTimeLeft(bidDate: Date): string {

    var date = new Date(bidDate.toString());
    this.sumDiff = (date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);

    if (this.sumDiff < 1) {
      this.hourDiff=Math.round(this.sumDiff*24);
      return this.hourDiff+"h";
    }
    if (this.sumDiff < 7) {
      this.dayDiff = Math.round(this.sumDiff);
      this.hourDiff=Math.round((this.sumDiff-this.dayDiff)*24);
      return this.dayDiff+" days, "+this.hourDiff+"h";

    }
    else {
      this.weekDiff=Math.round(this.sumDiff/7);
      this.dayDiff = Math.round(this.sumDiff-this.weekDiff*7);
      if(this.dayDiff>0)
        return this.weekDiff+" weeks, "+this.dayDiff+" days";
        else {
          this.hourDiff = Math.round((this.sumDiff-this.weekDiff*7)*24);
          return this.weekDiff+" weeks, "+Math.abs(this.hourDiff)+"h";
        }
    }
    
    


  }

}




