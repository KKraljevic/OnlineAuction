import { Component, OnInit } from '@angular/core';
import { Category } from '../category';
import { CategoryService } from '../category-service.service';
import { Item } from '../item';
import { ItemService } from '../item.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  
  categories: Category[] = [];
  items: Item[] = [];

  constructor(private categoryService: CategoryService,private itemService: ItemService) {
  
  }
  ngOnInit(): void {
    this.categoryService.findAll().subscribe(data => {this.categories = data;});
    this.itemService.findAll().subscribe(data => {this.items = data;});
    }

  
}
