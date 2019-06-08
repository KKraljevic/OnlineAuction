import { Component, OnInit } from '@angular/core';
import { Category } from '../category';
import { CategoryService } from '../category-service.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  
  categories: Category[];
  subcategories: Category[];
  isCollapsed: boolean;
  canCollapse: boolean;
  parent_id:number;

  constructor(private categoryService: CategoryService) {
    this.isCollapsed=false;
  }
  ngOnInit(): void {
    this.categoryService.findAll().subscribe(data => {this.categories = data;});

  }

  onClick(id:number){
    this.parent_id=id;
    this.isCollapsed=!this.isCollapsed;
    this.categoryService.findAllSubcategories(id).subscribe
    (data => {
      if(data!=null)
      {
        this.subcategories = data;
      }  
      else
      {
        this.subcategories = null;
      }
    });
  }

}
