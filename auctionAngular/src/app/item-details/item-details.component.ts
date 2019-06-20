import { Component, OnInit } from '@angular/core';
import { Item } from '../item';
import { Router, ActivatedRoute } from '@angular/router';
import { ItemService } from '../item.service';
import { Bid } from '../bid';
import { User } from '../user';
import { AuthenticationService } from '../authentication.service';
import { UserService } from '../user-service.service';
import { map } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {

  currentUser: User;
  item: Item;
  itemBids: Bid[] = [];
  minPrice: number;
  id: number;
  bid: Bid;

  bidForm: FormGroup;
  submitted = false;
  error: string;

  constructor(private router: Router, private route: ActivatedRoute, private formBuilder: FormBuilder,
    private userService: UserService, private authenticationService: AuthenticationService, private itemService: ItemService) {
    this.item = new Item();
    this.bid = new Bid();
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"));
      this.itemService.findById(this.id).toPromise().then(data => {
        this.item = data;
        this.item.category = data.category;
        this.minPrice=data.startPrice+10;
      });
    });
    this.itemService.getItemsBids(this.id).toPromise().then(data => {
      this.itemBids = data;
    })
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
    });

    this.bidForm = this.formBuilder.group({
      price: ['', [Validators.required, Validators.min(100)]]
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

          this.router.navigate(["/profile"]);
        }
      });
  }

}
