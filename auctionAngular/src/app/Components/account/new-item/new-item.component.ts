import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Item } from 'src/app/Model/item';
import { Image } from 'src/app/Model/image';
import { FormBuilder, Validators, FormGroup, FormControl, ValidationErrors } from '@angular/forms';
import { Category } from 'src/app/Model/category';
import { CategoryService } from 'src/app/Services/category-service.service';
import { ItemService } from 'src/app/Services/item.service';
import { Country } from 'src/app/Model/country';
import { City } from 'src/app/Model/city';
import { NgbDateStruct, NgbDate, NgbDateNativeAdapter } from '@ng-bootstrap/ng-bootstrap';
import { AccountModule } from '../account.module';
import { Shipping } from 'src/app/Model/shipping';
import { UserService } from 'src/app/Services/user-service.service';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { User } from 'src/app/Model/user';
import { FileUploader, FileItem, FileLikeObject } from 'ng2-file-upload';

@Component({
  selector: 'app-new-item',
  templateUrl: './new-item.component.html',
  styleUrls: ['./new-item.component.css']
})
export class NewItemComponent implements OnInit {

  currentUser: User;

  @ViewChild('fileInput', { static: true }) fileInput: ElementRef;

  uploader: FileUploader;
  isDropOver: boolean;
  imageUrlPath: SafeUrl;
  errorMessage: string;
  imgURLs: string[];

  categories: Category[];
  selectedCategory: Category;
  selectedSubcategory: Category;

  allCountries: Country[];
  countries: Country[];
  selectedCountry: Country;
  searchCountry: string;

  cities: City[];
  selectedCity: City;
  newItem: Item = new Item();


  progress: number = 1;
  titles: string[] = ["DETAIL INFORMATION ABOUT PRODUCT", "PRICE & DATE", "LOCATION & SHIPPING"];
  step1Form: FormGroup;
  step2Form: FormGroup;
  step3Form: FormGroup;
  submitted = false;
  next = false;

  datePattern = new RegExp("^\\d{4}-\\d{2}-\\d{2}$");
  now: Date = new Date();
  minDate = { year: this.now.getFullYear(), month: this.now.getMonth(), day: this.now.getDate() };

  constructor(private sanitizer: DomSanitizer, private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router,
    private categoryService: CategoryService, private itemService: ItemService, private userService: UserService,
    private authenticationService: AuthenticationService) {

    this.newItem.images = [];

    this.step1Form = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(60)]),
      cat: new FormControl({ value: '', disabled: true }, [Validators.required]),
      subCat: new FormControl({ value: '', disabled: true }, [Validators.required]),
      description: new FormControl('', [Validators.required, Validators.maxLength(250)]),
      images: new FormControl('', [Validators.required, Validators.min(4)]),
    });
    let pricePattern = "^\\d+\\.?\\d{0,2}$";
    this.step2Form = new FormGroup({
      price: new FormControl({ value: '' }, [Validators.required, Validators.pattern(pricePattern)]),
      endDate: new FormControl({ value: '' }, [Validators.required])
    });
    this.step3Form = new FormGroup({
      address: new FormControl('', [Validators.required, Validators.maxLength(250)]),
      country: new FormControl({ value: '', disabled: true }, [Validators.required]),
      city: new FormControl({ value: '', disabled: true }, [Validators.required]),
      zipCode: new FormControl('', [Validators.required, Validators.maxLength(10)]),
      freeshippment: new FormControl()
    });

  }

  ngOnInit(): void {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
    });

    this.categoryService.findAll().subscribe(data => {
      this.categories = data;
      this.step1Form.get('cat').enable();
    });
    this.itemService.getCountries().subscribe(data => {
      this.allCountries = data;
      this.countries = data;
      this.step3Form.get('country').enable();
    });

    const headers = [{ name: 'Accept', value: 'application/json' }];
    this.uploader = new FileUploader({
      url: '/api/uploadImage', autoUpload: false, headers: headers,
      allowedMimeType: ['image/jpeg', 'image/png'], maxFileSize: 1048000, queueLimit: 4
    });
    this.uploader.onAfterAddingFile = () => this.errorMessage = "";
  }

  onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
    switch (filter.name) {
      case 'fileSize':
        this.errorMessage = `Maximum upload size exceeded (${item.size} of 104800 allowed)`;
        break;
      case 'mimeType':
        this.errorMessage = `Type is not allowed. Allowed types: .jpeg, .png`;
        break;
        case 'queueLimit':
        this.errorMessage = `Maximum upload size (4 files) exceeded`;
        break;
      default:
        this.errorMessage = `Unknown error (filter is ${filter.name})`;
    }
  }

  preview(fileItem): SafeUrl {

    fileItem.withCredentials = false;
    return this.sanitizer.bypassSecurityTrustUrl((window.URL.createObjectURL(fileItem._file)));
  }

  uploadImage() {
    for (let fi of this.uploader.queue) {
      let data = new FormData();
      let fileItem = fi._file;
      console.log(fileItem.name);
      data.append('file', fileItem);
      this.itemService.uploadImage(data).subscribe(data => {
        console.log(data);
        let img: Image = new Image();
        img.url = data.toString();
        this.newItem.images.push(img);
        this.uploader.removeFromQueue(fi);
      });
    }
    //this.uploader.clearQueue();
  }

  fileOverAnother(e): void {
    this.isDropOver = e != undefined ? true : false;
  }

  fileClicked() {
    this.fileInput.nativeElement.click();
  }

  get f1() { return this.step1Form.controls; }
  get f2() { return this.step2Form.controls; }
  get f3() { return this.step3Form.controls; }

  backStep() {
    this.next = false;
    this.progress = this.progress - 1;
  }
  nextStep() {
    this.next = true;

    if (this.progress == 1) {
      this.f1.images.setValue(this.uploader.queue.length);
      if (this.step1Form.invalid) {
        console.log("Neispravna 1");
        this.getFormValidationErrors(this.step1Form);
        return;
      }
    }
    else if (this.progress == 2) {
      if (this.f2.endDate.value < this.now) {
        this.f2.endDate.setErrors({ 'incorrect': true });
      }
      if (this.step2Form.invalid) {
        console.log("Neispravna 2");
        this.getFormValidationErrors(this.step2Form);
        return;
      }
    }

    this.progress = this.progress + 1;
    this.next = false;

  }
  chooseCategory(c: Category) {
    this.selectedCategory = c;
    this.selectedSubcategory = null;
    this.step1Form.get('cat').setValue(this.selectedCategory.categoryName);
    this.step1Form.get('subCat').enable();
  }
  chooseSubcategory(c: Category) {
    this.selectedSubcategory = c;
    this.step1Form.get('subCat').setValue(this.selectedSubcategory.categoryName);
    console.log("Subcat: " + this.f1.subCat.value)
  }

  chooseCountry(c: Country) {
    this.selectedCountry = c;
    this.selectedCity = null;
    this.step3Form.get('country').setValue(this.selectedCountry.iso3);
    this.itemService.getCountryCities(c.iso3).subscribe(c => {
      this.cities = c,
      this.step3Form.get('city').enable();
    });
  }
  chooseCity(c: City) {
    this.selectedCity = c;
    this.step3Form.get('city').setValue(this.selectedCity.name);
  }

  checkBox() {
    console.log(this.f3.freeshippment.value);
  }

  filterCountries() {
    if (this.searchCountry != "") {
      this.countries = this.allCountries.filter(c => c.fullName.includes(this.searchCountry))
    }
    else {
      this.countries = this.allCountries;
    }
  }

  addItem() {
    this.next = true;
    if (this.step1Form.invalid || this.step2Form.invalid || this.step3Form.invalid) {
      this.getFormValidationErrors(this.step1Form);
      this.getFormValidationErrors(this.step2Form);
      this.getFormValidationErrors(this.step3Form);
      return;
    }

    for (let fi of this.uploader.queue) {
      let data = new FormData();
      let fileItem = fi._file;
      console.log(fileItem.name);
      data.append('file', fileItem);
      this.itemService.uploadImage(data).subscribe(data => {
        console.log(data);
        let img: Image = new Image();
        img.url = data.toString();
        this.newItem.images.push(img);
        this.uploader.removeFromQueue(fi);
        if (this.uploader.queue.length == 0) {

          this.newItem.name = this.f1.name.value;
          this.newItem.description = this.f1.description.value;
          this.newItem.category = this.selectedSubcategory;


          /*let imgs: Image[] = [];
          let placeholderImg = new Image();
          placeholderImg.url = "https://www.deadlineclaims.com/wp-content/uploads/2017/02/placeholder-image.jpg";
          imgs.push(placeholderImg);
          this.newItem.images = imgs;*/

          this.newItem.startPrice = this.f2.price.value;
          this.newItem.currentPrice = this.f2.price.value;
          this.newItem.endDate = this.f2.endDate.value;

          let shippment: Shipping = new Shipping();
          shippment.address = this.f3.address.value;
          shippment.zipcode = this.f3.zipCode.value;
          shippment.freeShipping = this.f3.freeshippment.value;
          shippment.city = this.cities.find(c => c.name == this.f3.city.value);
          this.newItem.shipping = shippment;

          this.userService.saveItem(this.newItem, this.currentUser.id).subscribe(item => {
            if (item != null) {
              this.router.navigate(['/item', item.id]);
            }
            else
              alert("Greska u dodavanju novog proizvoda!");
          });
        }
      });

    }

  }

  checkDate() {
    if (this.f2.endDate.value instanceof Date) {
      if (this.f2.endDate.value < this.now)
        return false;
      return true;
    }
    else if (this.datePattern.test(this.f2.endDate.value.toString())) {
      let date = new Date(this.f2.endDate.value.toString());
      this.f2.endDate.setValue(date);
      return true;
    }
    return false;
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
