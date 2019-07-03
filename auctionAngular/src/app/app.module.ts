import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { AppComponent } from './app.component';
import { UserProfileComponent } from './Components/user-profile/user-profile.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { UserService } from './Services/user-service.service';
import { LoginFormComponent } from './Components/login-form/login-form.component';
import { LandingPageComponent } from './Components/landing-page/landing-page.component';
import { AppHeaderComponent } from './Components/app-header/app-header.component';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './Components/registration/registration.component';
import { AppFooterComponent } from './Components/app-footer/app-footer.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ItemDetailsComponent } from './Components/item-details/item-details.component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { ShopComponent } from './Components/shop/shop.component';


@NgModule({
  declarations: [
    AppComponent,
    UserProfileComponent,
    LoginFormComponent,
    LandingPageComponent,
    AppHeaderComponent,
    RegistrationComponent,
    AppFooterComponent,
    ItemDetailsComponent,
    ShopComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    AppRoutingModule,
    AngularFontAwesomeModule,
    NgbModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
