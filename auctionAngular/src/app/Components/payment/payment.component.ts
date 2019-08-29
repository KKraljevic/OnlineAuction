import { Component, OnInit, TemplateRef, ChangeDetectorRef, ViewChild, NgZone, ElementRef } from '@angular/core';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { ItemService } from 'src/app/Services/item.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/Model/user';
import { Item } from 'src/app/Model/item';
import { Location } from 'src/app/Model/location';
import { FormGroup, FormControl, Validators, ValidationErrors } from '@angular/forms';
import { Country } from 'src/app/Model/country';
import { City } from 'src/app/Model/city';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { UserService } from 'src/app/Services/user-service.service';
import { Rating } from 'src/app/Model/rating';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  @ViewChild('template', { static: false }) public templateref: TemplateRef<any>;

  currentUser: User;
  userRate: number = 0;
  loadedUser: boolean = false;
  item: Item;
  cardToken: string ='';
  newRating: Rating;
  newShipping: Location;
  id: number;
  currentImage: string;
  modalRef: BsModalRef;
  stripeCheck: boolean=false;

  shippmentForm: FormGroup;
  cardForm: FormGroup;

  allCountries: Country[];
  countries: Country[];
  selectedCountry: Country;
  searchCountry: string;

  cities: City[];
  selectedCity: City;
  submitted: boolean = false;
  cardErrorMsg: string;

  currentYear: number = new Date().getFullYear();
  currentMonth: number = new Date().getMonth();
  years: number[] = [];
  months: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

  constructor(private authenticationService: AuthenticationService, private itemService: ItemService, private userService: UserService,
    private route: ActivatedRoute, private router: Router, private modalService: BsModalService,
     private changeDetection: ChangeDetectorRef, private _zone: NgZone) {

    this.shippmentForm = new FormGroup({
      address: new FormControl('', [Validators.required, Validators.maxLength(250)]),
      country: new FormControl({ value: '', disabled: true }, [Validators.required]),
      city: new FormControl({ value: '', disabled: true }, [Validators.required]),
      zipCode: new FormControl('', [Validators.required, Validators.maxLength(10)]),
      freeShippment: new FormControl()
    });
    this.cardForm = new FormGroup({
      cardHolder: new FormControl('', [Validators.required, Validators.maxLength(30)]),
      cardNumber: new FormControl('', [Validators.required, Validators.maxLength(16), Validators.minLength(14), Validators.pattern('[0-9]*')]),
      expMonth: new FormControl('', [Validators.required]),
      expYear: new FormControl('', [Validators.required, Validators.min(this.currentYear)]),
      cvc: new FormControl('', [Validators.required, Validators.pattern('\\d{3,4}')])
    });
    this.newRating = new Rating();
    this.newShipping = new Location();
    for (let index = 0; index < 5; index++) {
      this.years.push(this.currentYear + index);
    }
  }

  ngOnInit() {

    this.itemService.getCountries().subscribe(data => {
      this.allCountries = data;
      this.countries = data;
      this.shippmentForm.get('country').enable();
    });

    this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"));

      this.itemService.findById(this.id).toPromise().then(data => {
        this.item = data;
        this.item.category = data.category;
        this.currentImage = this.item.images[0].url;

        this.authenticationService.currentUser.subscribe(x => {
          this.currentUser = x;
          this.newRating.bidder = x.id;
          this.loadedUser = true;
        });
      });
    });
  }

  get f() { return this.shippmentForm.controls; }


  chooseCountry(c: Country) {
    this.selectedCountry = c;
    this.selectedCity = null;    
    this.shippmentForm.get('city').setValue('');
    this.shippmentForm.get('city').disable();
    this.shippmentForm.get('country').setValue(this.selectedCountry.iso3);
    this.itemService.getCountryCities(c.iso3).subscribe(c => {
      this.cities = c,
        this.shippmentForm.get('city').enable();
    });
  }
  chooseCity(c: City) {
    this.selectedCity = c;
    this.shippmentForm.get('city').setValue(this.selectedCity.name);
  }

  chooseYear(year: number) {
    if (year === this.currentYear) {
      this.cardForm.get('expMonth').clearValidators();
      this.cardForm.get('expMonth').setValidators([Validators.required, Validators.min(this.currentMonth + 2)]);
    }
    else {
      this.cardForm.get('expMonth').clearValidators();
      this.cardForm.get('expMonth').setValidators(Validators.required);
    }
    this.cardForm.get('expMonth').setValue(this.cardForm.get('expMonth').value);
    this.cardForm.get('expYear').setValue(year);
  }

  filterCountries() {
    if (this.searchCountry != "") {
      this.countries = this.allCountries.filter(c => c.fullName.includes(this.searchCountry))
    }
    else {
      this.countries = this.allCountries;
    }
  }

  finishPayment() {
    this.submitted = true;
    if (!this.shippmentForm.valid || !this.cardForm.valid) {
      this.getFormValidationErrors(this.shippmentForm);
      this.getFormValidationErrors(this.cardForm);
      return;
    }
    //this.cardErrorMsg=(<any>window).Stripe.card.validateCardNumber(this.cardForm.get('cardNumber').value);
    this.newShipping.address = this.f.address.value;
    this.newShipping.zipcode = this.f.zipCode.value;
    this.newShipping.freeShipping = this.f.freeShippment.value;
    this.newShipping.city = this.cities.find(c => c.name == this.f.city.value);

    this.tokenizeCard();

    /*this.itemService.addPaymentAndShipping(this.cardToken, this.item.currentPrice, this.item).subscribe(data => {
      if(data!=null){
        console.log(data);
        this.openModal(this.templateref);
      }
    })*/
    //let resp=this.itemService.chargeCard(this.cardToken, this.item.currentPrice);
    //console.log(resp);
  }
  tokenizeCard() {
    this.stripeCheck=true;
    this.cardErrorMsg='Checking your card...';
  
    (<any>window).Stripe.card.createToken({
      number: this.cardForm.get('cardNumber').value,
      exp_month: this.cardForm.get('expMonth').value,
      exp_year: this.cardForm.get('expYear').value,
      cvc: this.cardForm.get('cvc').value
    }, (status: number, response: any) => {
      this._zone.run(() => {
      if (status === 200) {
        this.cardToken = response.id;
        this.cardErrorMsg="Card is verified! Wait for pop-up to rate the seller."
        console.log(this.newShipping);
        this.itemService.addPaymentAndShipping(response.id, this.item.currentPrice, this.item.id, this.newShipping).subscribe(data => {
          if (data != null) {
            console.log(data);
            this.openModal(this.templateref);
          }
        });
      } else {
        this.stripeCheck=false;
        this.cardToken = '';
        this.cardErrorMsg=response.error.message;
        console.log(response.error.message);
      }
      return;
    });
    });
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, { class: 'modal-md' });
    this.modalService.onHide.subscribe(response => {
      this.router.navigate(['/item', this.id]);
    });
  }

  confirm(id: number): void {
    this.newRating.value = this.userRate;
    this.userService.addUserRating(this.item.seller.id, this.newRating).subscribe(data=>{
      if(data!=null){
        console.log(data);
        this.modalRef.hide();
      }
    })
  }
  decline(): void {
    this.modalRef.hide();
  }

  getFormValidationErrors(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {

      const controlErrors: ValidationErrors = form.get(key).errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          console.log('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
        });
      }
    });
  }

}
