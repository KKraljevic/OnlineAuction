import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { AppComponent } from './app.component';
import { UserProfileComponent } from './Components/account/user-profile/user-profile.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { UserService } from './Services/user-service.service';
import { LoginFormComponent } from './Components/login-form/login-form.component';
import { LandingPageComponent } from './Components/landing-page/landing-page.component';
import { AppHeaderComponent } from './Components/app-header/app-header.component';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './Components/registration/registration.component';
import { AppFooterComponent } from './Components/app-footer/app-footer.component';
import {NgbModule, NgbDateAdapter, NgbDateNativeAdapter} from '@ng-bootstrap/ng-bootstrap';
import { ItemDetailsComponent } from './Components/item-details/item-details.component';
import { ShopComponent } from './Components/shop/shop.component';
import { AccountComponent } from './Components/account/account/account.component';
import { AccountModule } from './Components/account/account.module';
import { ItemService } from './Services/item.service';
import { CategoryService } from './Services/category-service.service';
import { BidService } from './Services/bid.service';
import {FileUploadModule,FileDropDirective, FileSelectDirective} from 'ng2-file-upload';




@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    LandingPageComponent,
    AppHeaderComponent,
    RegistrationComponent,
    AppFooterComponent,
    ItemDetailsComponent,
    ShopComponent,
    AccountComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AccountModule,
    RouterModule,
    AppRoutingModule,
    AngularFontAwesomeModule,
    NgbModule
  ],
 
  providers: [UserService,ItemService,CategoryService,BidService,{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter}],
  bootstrap: [AppComponent]
})
export class AppModule { }
