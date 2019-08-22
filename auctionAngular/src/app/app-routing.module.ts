import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { UserProfileComponent } from './Components/account/user-profile/user-profile.component';
import { LoginFormComponent } from './Components/login-form/login-form.component';
import { LandingPageComponent } from './Components/landing-page/landing-page.component';
import { RegistrationComponent } from './Components/registration/registration.component';
import { AuthGuard } from './auth-guard';
import { ItemDetailsComponent } from './Components/item-details/item-details.component';
import { ShopComponent } from './Components/shop/shop.component';
import { AccountComponent } from './Components/account/account/account.component';
import { PaymentComponent } from './Components/payment/payment.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'account', component: AccountComponent},
  { path: 'registration', component: RegistrationComponent, canActivate: [AuthGuard] },
  { path: 'payment/:id',component: PaymentComponent},
  { path: 'item/:id/*', component: ItemDetailsComponent },
  { path: 'item/:id', component: ItemDetailsComponent },
  { path: 'shop/:cat/:subcat', component: ShopComponent },
  { path: 'search/:search', component: ShopComponent },
  { path: 'shop', component: ShopComponent},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }





