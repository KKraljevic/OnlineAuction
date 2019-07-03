import { Component, OnInit } from '@angular/core';
import { Item } from '../../Model/item';
import { Router, ActivatedRoute } from '@angular/router';
import { ItemService } from '../../Services/item.service';
import { Bid } from '../../Model/bid';
import { User } from '../../Model/user';
import { AuthenticationService } from '../../Services/authentication.service';
import { UserService } from '../../Services/user-service.service';
import { map } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {

  currentUser: User;
  item: Item;
  itemBids: Bid[];
  hasBids: boolean = false;
  id: number;
  bid: Bid;

  bidForm: FormGroup;
  submitted = false;
  error: string;
  highest: any;
  show: boolean = false;
  message: string;

  constructor(private router: Router, private route: ActivatedRoute, private formBuilder: FormBuilder,
    private userService: UserService, private authenticationService: AuthenticationService, private itemService: ItemService) {
    this.item = new Item();
    this.bid = new Bid();
    
    this.bidForm = this.formBuilder.group({
      price: ['',]
    });

  }

  ngOnInit() {

    this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"));

      this.itemService.findById(this.id).toPromise().then(data => {
        this.item = data;
        this.item.category = data.category;

        this.bidForm.controls.price.setValidators([Validators.required, Validators.min(data.currentPrice + 10)]);
      });
    });
    this.itemService.getItemsBids(this.id).toPromise().then(data => {
      if (data != null) {
        this.itemBids = data;
        this.hasBids = true;
      }
    });
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
    });

  }

  get f() { return this.bidForm.controls; }

  makeBid() {
    this.submitted = true;

    if (this.bidForm.invalid) {
      return;
    }
    this.bid.bidPrice = this.bidForm.get('price').value;
    this.bid.bidTime = new Date();
    this.bid.active = true;

    this.userService.saveBid(this.bid, this.currentUser.id, this.item.id).toPromise().then(
      r => {
        if (r == null)
          alert("Bid not correct!");
        else {

          this.refreshData();
          // this.router.navigate(['/item',this.id, this.highest]);
          this.show = true;
        }
      });
  }

  refreshData() {

    this.itemService.findById(this.id).toPromise().then(data => {
      this.item = data;
      if (this.bid.bidPrice == data.currentPrice) {
        this.highest = true;
      }
      else {
        this.highest = false;
      }
    });

    this.itemService.getItemsBids(this.id).toPromise().then(data => {
      if (data != null) {
        this.itemBids = data;
        this.hasBids = true;
      }
    });

    this.bidForm.controls.price.setValidators([Validators.required, Validators.min(this.item.currentPrice + 10)]);


  }
}
