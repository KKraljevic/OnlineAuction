import { Component, OnInit } from '@angular/core';
import { Item } from '../item';
import { Router, ActivatedRoute } from '@angular/router';
import { ItemService } from '../item.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {

  item: Item;
  id:number;
  daysLeft:number;
  constructor( private router:Router, private route: ActivatedRoute, private itemService: ItemService) 
  { 
    this.item= new Item();
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"))
    });
    this.itemService.findById(this.id).subscribe(data=>{this.item=data;});
    //this.daysLeft=this.getDiferenceInDays(this.item.endDate);
  }

  getDiferenceInDays(theDate : Date) : number {
    //this.item.endDate.setTime(0);
    return Math.abs(theDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24) ;
}

}
