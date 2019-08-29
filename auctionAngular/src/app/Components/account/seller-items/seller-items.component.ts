import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { UserService } from 'src/app/Services/user-service.service';
import { User } from 'src/app/Model/user';
import { Item } from 'src/app/Model/item';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-seller-items',
  templateUrl: './seller-items.component.html',
  styleUrls: ['./seller-items.component.css']
})
export class SellerItemsComponent implements OnInit {

  currentUser: User;
  items: Item[] = [];
  hasItems: boolean;
  show: boolean = false;

  activeTab: number = 0;
  msg: string;

  currentPage: number = 0;
  totalPages: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService, private route: ActivatedRoute) {
  }
  ngOnInit() {
    this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;

      this.userService.getItems(user.id).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        if (data['totalElements'] === 0)
          this.hasItems = false;
        else
          this.hasItems = true;
        this.show = true;
      },
        error => this.show = true
      );
    });
  }
  loadItems(page: number, tab?: number) {
    this.activeTab = tab !== undefined ? tab : this.activeTab;
    this.items = null;
    this.userService.getItems(this.currentUser.id, page, this.activeTab).subscribe(data => {
      this.items = data['content'];
      this.totalPages = data['totalPages'];
      this.currentPage = data['number'];
    });
  }
  changeTab(msg: string) {
    this.msg = msg;
  }
  refreshItems(changed: boolean) {
    if (changed) {
      this.loadItems(this.currentPage);
    }
  }
}
