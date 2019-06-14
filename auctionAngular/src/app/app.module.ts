import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { AppComponent } from './app.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { HttpClientModule } from '@angular/common/http';
import { UsersListComponent } from './users-list/users-list.component';
import { AppRoutingModule } from './app-routing.module';
import { UserService } from './user-service.service';
import { LoginFormComponent } from './login-form/login-form.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { AppHeaderComponent } from './app-header/app-header.component';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { AppFooterComponent } from './app-footer/app-footer.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ItemDetailsComponent } from './item-details/item-details.component';

@NgModule({
  declarations: [
    AppComponent,
    UserProfileComponent,
    UsersListComponent,
    LoginFormComponent,
    LandingPageComponent,
    AppHeaderComponent,
    RegistrationComponent,
    AppFooterComponent,
    ItemDetailsComponent
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
