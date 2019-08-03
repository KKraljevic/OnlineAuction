import { Component, OnInit, Input } from '@angular/core';
import { Item } from 'src/app/Model/item';
import { User } from 'src/app/Model/user';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { UserService } from 'src/app/Services/user-service.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css', '../user-profile/user-profile.component.css']
})
export class WishlistComponent implements OnInit {

  currentUser: User;
  wishlist: Item[] = [];
  hasWishlist: boolean;
  show: boolean;

  totalPages:number;
  currentPage:number =0;

  sumDiff: number;
  hourDiff: number;
  dayDiff: number;
  weekDiff: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService) { }

  ngOnInit(): void {

    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;

      this.userService.getWishlist(this.currentUser.id).subscribe(data => {
        this.wishlist = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];        
        if (this.wishlist.length===0)
          this.hasWishlist = false;
        else {
          this.hasWishlist = true;
        }
      },
        error => this.show=true,
        () => this.show=true);
    });

  }

  isActive(endDate: Date): boolean {
    var date = new Date(endDate.toString());
    let diff = Math.round(date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60);

    return diff === 0 ? false : true;
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

  loadItems(page: number) {
    this.userService.getWishlist(this.currentUser.id, page).subscribe(
      data => {
        this.wishlist = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
      }
    )
  }

}
