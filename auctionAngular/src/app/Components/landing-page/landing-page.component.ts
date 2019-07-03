import { Component, OnInit } from '@angular/core';
import { Category } from '../../Model/category';
import { CategoryService } from '../../Services/category-service.service';
import { Item } from '../../Model/item';
import { ItemService } from '../../Services/item.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  categories: Category[] = [];
  items: Item[] = [];

  constructor(private categoryService: CategoryService, private itemService: ItemService) {
  }
  ngOnInit(): void {
    this.categoryService.findAll().subscribe(data => { this.categories = data; });

    this.itemService.findFeaturedItems().subscribe(data => {
      this.items = data;
    });
  }


}
