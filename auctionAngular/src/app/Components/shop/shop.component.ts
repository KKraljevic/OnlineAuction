import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/Services/category-service.service';
import { ItemService } from 'src/app/Services/item.service';
import { Item } from 'src/app/Model/item';
import { Category } from 'src/app/Model/category';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from 'src/app/Model/user';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { UserService } from 'src/app/Services/user-service.service';
import { Subscription, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {

  searchMode: boolean;
  searchInput: string;
  msgNotFound: boolean;

  sub: Subscription;
  currentUser: User;
  wishlist: Item[];

  items: Item[] = [];
  categories: Category[] = [];
  currentCategory: Category;
  id: number;
  showGrid: boolean = true;

  sortOrders: string[] = ["Default Sorting", "Sort by Name", "Sort by Price"];
  selectedSortOrder: string = this.sortOrders[0];
  sortMode: number = 0;
  totalPages: number;
  currentPage: number = 0;

  constructor(private router: Router, private route: ActivatedRoute, private authenticationService: AuthenticationService,
    private categoryService: CategoryService, private itemService: ItemService, private userService: UserService, ) { }

  ngOnInit() {

    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      if (x != null) {
        this.userService.getAllWishlist(x.id).subscribe(wl => {
          this.wishlist = wl;
        });
      }
    });

    this.categoryService.findAll().subscribe(data => {
      this.categories = data;
    });

    this.sub = this.route.paramMap.subscribe(params => {
      if (!params.has('search')) {

        this.searchMode = false;

        if (params.get("cat") != null && params.get("subcat") != null) {
          return this.categoryService.findCategoryByName(params.get("subcat")).subscribe(cat => {
            this.currentCategory = cat;
            console.log("OnInit: " + this.currentCategory.categoryName);
            this.loadCategoryItems(this.currentCategory.id, 0, undefined);
          });
        }
        else {
          this.loadCategoryItems(0, 0, 0);
        }
      }
      else {
        this.searchMode = true;
        this.searchInput = params.get('search');
        this.searchInput = this.searchInput != null ? this.searchInput : "";
        this.loadCategoryItems(0, 0, 0);
      }
    });
  }

  loadCategoryItems(id: number, page?: number, sort?: number) {
    if (Number.isInteger(sort)) { this.sortMode = sort; }
    if (this.searchMode) {
      this.itemService.findItems(this.searchInput, page, this.sortMode).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        this.msgNotFound=this.items.length? false : true;
      });
    }
    else if (id > 0) {
      this.categoryService.findCategoryItems(id, page, this.sortMode).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        this.msgNotFound=this.items.length? false : true;
      });
    }
    else {
      this.currentCategory = null;
      this.itemService.findAll(page, sort).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        this.msgNotFound=this.items.length? false : true;
      });
    }
  }

  ChangeSortOrder(newSortOrder: string) {
    this.selectedSortOrder = newSortOrder;
    this.sortMode = this.sortOrders.indexOf(newSortOrder);
      if (this.currentCategory == null) {
        this.loadCategoryItems(0, 0, this.sortMode);
      }
      else {
        this.loadCategoryItems(this.currentCategory.id, 0, this.sortMode);
      }
  }

  addToWishlist(item: Item) {
    if (!this.checkWishlist(item)) {
      this.userService.saveWishlist(this.currentUser.id, item.id).subscribe(
        result => {
          if (result) {
            this.wishlist=result;
            console.log("WishlistItem Added");
          }
          else
            alert("Greska u dodavanju");
        });
    }
    if (this.wishlist === undefined)
      this.wishlist = [];

    this.wishlist.push(item);
    console.log("Dodat: " + item.id);
  }

  checkWishlist(i: Item) {
    if (this.wishlist == undefined) {
      return false;
    }
    else
      return this.wishlist.find(item => item.id === i.id);
  }
  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}


