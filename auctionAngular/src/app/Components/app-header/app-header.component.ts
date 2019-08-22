import { Component, OnInit, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute, Event, NavigationEnd, NavigationStart } from '@angular/router';
import { User } from '../../Model/user';
import { AuthenticationService } from '../../Services/authentication.service';
import { UserService } from 'src/app/Services/user-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.css']
})

export class AppHeaderComponent implements OnInit {

  currentUser: User;
  hasWishlist: boolean;
  hasBids: boolean;
  hasItems: boolean;
  pendingItems: string;
  wonBids: string;
  notificationMsg: string;
  path: any;

  searchForm: FormGroup;
  submitted: boolean = false;

  constructor(private router: Router, private authenticationService: AuthenticationService, private userService: UserService,
    private fb: FormBuilder) {

    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.path = event.url;
      }
    });
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;
      console.log(this.currentUser);
      if (this.currentUser != null) {
        //this.userService.hasBids(user.id).subscribe(res => this.hasBids = res);
        //this.userService.hasItems(user.id).subscribe(res => this.hasItems = res);
        //this.userService.hasWishlist(user.id).subscribe(res => this.hasWishlist = res);
        this.userService.countPendingItems(user.id).toPromise().then(res => {
          console.log(res);
          this.pendingItems = res.toString();
          if (this.pendingItems === '1') {
            this.pendingItems += ' item with expired auction date';
          }
          else if (this.pendingItems != '0') {
            this.pendingItems += ' items with expired auction date';
          }
        });
        this.userService.countWonBids(user.id).toPromise().then(res => {
          console.log(res);
          this.wonBids = res.toString();
          if (this.wonBids === '1') {
            this.wonBids += ' auction that you have won!';
          }
          else if (this.wonBids != '0') {
            this.wonBids += ' auctions that you have won!';
          }
        });
      }
    });
    this.searchForm = this.fb.group({
      search: ['']
    });
    //this.getNotificationMsg();

  }

  ngAfterContentInit() {
    this.getNotificationMsg();
  }

  getNotificationMsg() {
    if (this.pendingItems && this.wonBids) {
      if (this.pendingItems[0] != '0' && this.wonBids[0] != '0') {
        this.notificationMsg = this.pendingItems + " and " + this.wonBids;
      }
      else if (this.pendingItems[0] != '0') {
        this.notificationMsg = this.pendingItems + "!";
      }
      else if (this.wonBids[0] != '0') {
        this.notificationMsg = this.wonBids;
      }
    }
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  searchItems() {
    this.submitted = true;

    if (this.searchForm.invalid) {
      return;
    }

    this.router.navigate(['/search/', this.searchForm.get('search').value]);
  }

}
