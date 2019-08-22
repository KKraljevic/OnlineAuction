import { Component, OnInit } from '@angular/core';
import { Item } from '../../Model/item';
import { Router, ActivatedRoute } from '@angular/router';
import { ItemService } from '../../Services/item.service';
import { Bid } from '../../Model/bid';
import { User } from '../../Model/user';
import { Image } from '../../Model/image';
import { AuthenticationService } from '../../Services/authentication.service';
import { UserService } from '../../Services/user-service.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CategoryService } from 'src/app/Services/category-service.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {

  currentUser: User;
  loadedUser: boolean = false;
  wishlist: Item[];
  item: Item;
  itemBids: Bid[];
  sellerRating: number = 0;
  highestBidder: User;
  relatedItems: Item[];
  hasBids: boolean;
  id: number;
  bid: Bid;

  currentPage: number = 0;
  totalPages: number;

  currentImage: string;

  bidForm: FormGroup;
  submitted = false;
  error: string;
  highest: any;
  expired: boolean;
  show: boolean;
  message: string;

  constructor(private router: Router, private route: ActivatedRoute, private formBuilder: FormBuilder,
    private userService: UserService, private authenticationService: AuthenticationService, private itemService: ItemService,
    private categoryService: CategoryService) {
    this.item = new Item();
    this.bid = new Bid();

    this.bidForm = this.formBuilder.group({
      price: ['', [Validators.required]]
    });

  }

  ngOnInit() {

    this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"));

      this.itemService.findById(this.id).toPromise().then(data => {
        this.item = data;
        this.item.category = data.category;
        var date = new Date(this.item.endDate.toString());
        let diff = (date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);
        this.expired = diff > 0.0 ? false : true;
        console.log("Difference days:" + diff);
        this.bidForm.controls.price.setValidators([Validators.required, Validators.min(data.currentPrice + 10), Validators.maxLength(10), Validators.pattern('[0-9]*\\.?[0-9]{0,2}')]);

        this.currentImage = this.item.images[0].url;
        this.addPlaceholderImgs();

        this.authenticationService.currentUser.subscribe(x => {
          this.currentUser = x;
          this.loadedUser = true;
          if (x != null) {
            this.userService.getAllWishlist(x.id).subscribe(wl => {
              this.wishlist = wl;
            });
            if (x.id === this.item.seller.id) {
              this.loadItemBids(0);
            }
            else {
              this.categoryService.findCategoryItems(this.item.category.id, 0, undefined).subscribe(items => {
                this.relatedItems = items['content'];
                this.relatedItems = this.relatedItems.filter(obj => obj.id !== this.id);

                if (this.relatedItems.length > 3)
                  this.relatedItems = this.relatedItems.slice(0, 3);
              });

              this.getHighestBidder(this.id);
              
              this.userService.getUserRating(this.item.seller.id).subscribe(rating =>{
                if(rating!=null){
                  this.sellerRating=Number.parseFloat(rating.toString());
                  if(this.sellerRating===NaN){
                    this.sellerRating=0;
                  }
                }
              });
            }
          }
        });
      });
    });

  }

  get f() { return this.bidForm.controls; }

  makeBid() {
    this.submitted = true;

    if (this.bidForm.invalid) {
      return;
    }
    else if (this.bidForm.get('price').untouched || this.bidForm.get('price').value < (this.item.currentPrice + 10)) {
      this.bidForm.get('price').setErrors({ incorrect: true });
      return;
    }
    var date = new Date(this.item.endDate.toString());
    let diff = (date.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24);
    if (diff < 0.5 || this.expired) {
      this.expired = true;
      return;
    }
    this.bid.bidPrice = this.bidForm.get('price').value;
    console.log("Price:" + this.bid.bidPrice);
    this.bid.bidTime = new Date();
    this.bid.active = true;

    this.userService.saveBid(this.bid, this.currentUser.id, this.item.id).toPromise().then(
      r => {
        if (r == null)
          alert("Bid not correct!");
        else {
          this.refreshData();
        }
      });
  }
  addPlaceholderImgs() {
    if (this.item.images.length % 4 != 0) {
      let plImg = 4 - this.item.images.length % 4;
      let placeholderImage: Image = new Image();
      placeholderImage.url = "https://res.cloudinary.com/kkraljevic/image/upload/v1563963969/abh/auction/xqoesld1fklyt4nawq1y.png";
      for (let index = 0; index < plImg; index++) {
        this.item.images.push(placeholderImage);
      }
    }
  }
  viewPrice() {
    console.log("View price:" + this.bidForm.get('price').value);
  }
  refreshData() {

    this.itemService.findById(this.id).toPromise().then(data => {
      this.item = data;
      this.addPlaceholderImgs();
      if (this.bid.bidPrice == data.currentPrice) {
        this.highest = true;
      }
      else {
        this.highest = false;
      }
      this.show = true;
    });

    this.loadItemBids(0);

    this.bidForm.controls.price.setValidators([Validators.required, Validators.min(this.item.currentPrice + 10)]);

  }

  addToWishlist(item: Item) {
    if (!this.checkWishlist(item)) {
      this.userService.saveWishlist(this.currentUser.id, item.id).subscribe(
        result => {
          if (result) {
            this.userService.getWishlist(this.currentUser.id).subscribe(wl =>
              this.wishlist = wl);
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

  loadItemBids(page: number) {
    this.itemService.getItemsBids(this.id, page).toPromise().then(data => {
      if (data != null) {
        this.itemBids = data['content'];
        this.currentPage = data['number'];
        this.totalPages = data['totalPages'];
        this.hasBids = data['totalElements'] ? true : false;
        if (this.hasBids) {
          this.highestBidder = this.itemBids[0].bidder;
        }
        else {
          this.highestBidder = null;
        }
      }
    });
  }

  getHighestBidder(id: number) {
    this.itemService.getHighestBidder(id).subscribe(data => {
      this.highestBidder = data;
      if(data.id!=this.currentUser.id && this.item.paid) {
        this.router.navigate(['/']);
      }
    });
  }

}
