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
        this.item.bids = data.bids;
        this.item.category = data.category;
      });
    });
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
    });

    this.bidForm = this.formBuilder.group({
      price: ['', [Validators.required, Validators.min(100)]]
    });
  }

  get f() { return this.bidForm.controls; }

  getDiferenceInDays(theDate: Date): number {
    return Math.abs(theDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
  }

  makeBid() {
    this.submitted = true;
    this.bid.bidPrice = this.bidForm.get('price').value;

    if (this.bidForm.invalid) {
      return;
    }

    this.bid.bidTime = new Date();
    this.bid.isActive = true;
    this.bid.item = this.item;
    this.userService.findById(this.currentUser.id).toPromise().then(user => {
      if (user != null) {
        this.bid.user = user;
        if (this.bid.bidPrice > this.item.currentPrice) {
          this.item.currentPrice = this.bid.bidPrice;
        }
        this.userService.saveBid(this.bid).toPromise().then(
          r => {
            if (r == null)
              alert("Bid not correct!");
            else {

              this.router.navigate(["/profile"]);
            }
          });
      }
      else
        alert("User not correct!");
    });
  }

}
/*this.itemService.updatePrice(this.item.item_id, this.item).toPromise().then(
                  r => {
                    if (r == null)
                      alert("Update not correct!");
                    else {
                      this.router.navigate(["/profile"]);
                    }
                  }
                );*/
