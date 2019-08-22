import { Component, OnInit, Input } from '@angular/core';
import { Bid } from 'src/app/Model/bid';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { User } from 'src/app/Model/user';
import { UserService } from 'src/app/Services/user-service.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-bids',
  templateUrl: './bids.component.html',
  styleUrls: ['../seller-items/seller-items.component.css']
})
export class BidsComponent implements OnInit {

  bids: Bid[] = [];
  hasBids: boolean;
  currentUser: User;
  show: boolean = false;
  activeTab: number = 0;

  totalPages: number;
  currentPage: number = 0;

  constructor(private authenticationService: AuthenticationService, private userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      this.userService.getBids(this.currentUser.id, 0, this.activeTab).subscribe(b => {
        this.bids = b['content'];
        this.totalPages = b['totalPages'];
        this.currentPage = b['number']
        if (b['totalElements'] === 0)
          this.hasBids = false;
        else {
          this.hasBids = true;
        }
        this.show = true;
      },
        error => this.show = true
      );
    });
  }

  loadBids(page: number, tab?: number) {
    this.activeTab = tab != undefined ? tab : this.activeTab;
    this.bids = null;
    this.userService.getBids(this.currentUser.id, page, this.activeTab).subscribe(
      data => {
        this.bids = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
      });
  }

}
