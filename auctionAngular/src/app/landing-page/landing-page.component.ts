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
  
  categories: Category[];
  subcategories: Category[];
  isCollapsed: boolean;
  parent_id:number;

  items: Item[];

  constructor(private categoryService: CategoryService,private itemService: ItemService) {
    this.isCollapsed=false;
  }
  ngOnInit(): void {
    this.categoryService.findAll().subscribe(data => {this.categories = data;});
    this.itemService.findAll().subscribe(data => {this.items = data;});
  }

  onClickCategory(id:number){
    this.parent_id=id;
    this.isCollapsed=!this.isCollapsed;
    this.subcategories=this.categories.find(x => x.category_id === id).children;
  }
  
}
