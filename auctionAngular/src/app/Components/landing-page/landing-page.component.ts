import { Component, OnInit } from '@angular/core';
import { Category } from '../../Model/category';
import { CategoryService } from '../../Services/category-service.service';
import { Item } from '../../Model/item';
import { ItemService } from '../../Services/item.service';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { User } from 'src/app/Model/user';
import { UserService } from 'src/app/Services/user-service.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  currentUser: User;
  categories: Category[] = [];
  items: Item[] = [];
  sellerItems = [];
  otherItems: Item[] = [];
  featuredItem: Item;

  constructor(private categoryService: CategoryService, private itemService: ItemService,
    private authenticationService: AuthenticationService, private userService: UserService) {
  }
  ngOnInit(): void {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      if (x != null) {
        this.userService.getUsersItems(x.id).toPromise().then(
          items => {
            this.sellerItems = items['content'];
            if (this.sellerItems.length > 4)
              this.sellerItems = this.sellerItems.slice(0, 4);
          });
      }
    });

    this.categoryService.findAll().subscribe(data => { this.categories = data; });

    this.itemService.findFeaturedItems(this.currentUser ? this.currentUser.id : undefined).subscribe(data => {
      this.items = data;
      this.featuredItem = this.items[0];
    });

    this.itemService.findAll(0, undefined, this.currentUser ? this.currentUser.id : undefined).subscribe(data => {
      this.otherItems = data['content'];
      if (this.otherItems.length >= 5)
        this.otherItems = this.otherItems.slice(0, 4);
    });

  }


}
