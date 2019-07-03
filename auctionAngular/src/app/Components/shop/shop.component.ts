import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/Services/category-service.service';
import { ItemService } from 'src/app/Services/item.service';
import { Item } from 'src/app/Model/item';
import { Category } from 'src/app/Model/category';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {

  items: Item[] = [];
  categories: Category[] = [];
  currentCategory: Category = new Category();
  id: number;
  showGrid: boolean = true;

  sortOrders: string[] = ["Default Sorting", "Sort by Name", "Sort by Price"];
  selectedSortOrder: string = this.sortOrders[0];
  sortMode: number = 0;
  totalPages: number;
  currentPage: number = 0;

  constructor(private router: Router, private route: ActivatedRoute, private categoryService: CategoryService, private itemService: ItemService) { }

  ngOnInit() {

    this.route.paramMap.subscribe(params => {
      if (params.get("id") != null) {

        this.id = Number.parseInt(params.get("id"));

        this.itemService.findCategoryItems(this.id, 0).subscribe(data => {
          this.items = data['content'];
          this.totalPages = data['totalPages'];
          this.currentPage = data['number'];
          this.currentCategory = this.items[0].category;
        });
      }
      else {
        this.itemService.findFeaturedItems().subscribe(data => {
          this.items = data;
        });
      }
    });

    this.categoryService.findAll().subscribe(data => {
      this.categories = data;
    });
  }

  loadCategoryItems(id: number, page: number, sort?: number) {
    if (id > 0) {
      if (sort) { this.sortMode = sort; }
      this.itemService.findCategoryItems(id, page, this.sortMode).subscribe(data => {
        this.items = data['content'];
        this.totalPages = data['totalPages'];
        this.currentPage = data['number'];
        this.currentCategory = this.items[0].category;
      });
    }
    else {
      /*All items with pagination*/
      this.itemService.findFeaturedItems().subscribe(data => {
        this.items = data;
      });
    }
  }

  ChangeSortOrder(newSortOrder: string) {
    this.selectedSortOrder = newSortOrder;
    this.sortMode = this.sortOrders.indexOf(newSortOrder);
    this.loadCategoryItems(this.currentCategory.id, 0, this.sortMode);
  }


}
