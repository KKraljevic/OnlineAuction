import { Component, OnInit, Input } from '@angular/core';
import { Item } from 'src/app/Model/item';
import { User } from 'src/app/Model/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/Services/user-service.service';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { error } from 'protractor';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {

  currentUser: User;
  items: Item[] = [];
  hasItems: boolean;
  show: boolean = false;

  currentPage: number = 0;
  totalPages: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;

      this.userService.getUsersItems(user.id).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        if (this.items.length===0)
          this.hasItems = false;
        else
          this.hasItems = true;
        this.show = true;
      },
        error => this.show = true
      );
    });
  }

  loadItems(page: number) {
    this.userService.getUsersItems(this.currentUser.id, page).subscribe(
      data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
      }
    )
  }
}
