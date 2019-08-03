import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from '../../app.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ItemsComponent } from './items/items.component';
import { BidsComponent } from './bids/bids.component';
import { AuthGuard } from 'src/app/auth-guard';
import { AccountComponent } from './account/account.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { NewItemComponent } from './new-item/new-item.component';

const accountRoutes: Routes = [
  { path: 'account', component: AccountComponent,
    children: [
      { path: 'profile',component: UserProfileComponent},
      { path: 'items',component: ItemsComponent},
      { path: 'newitem',component: NewItemComponent},
      { path: 'bids',component: BidsComponent},
      { path: 'addItem',component: UserProfileComponent},
      { path: 'wishlist',component: WishlistComponent}
    ]
  }
];

@NgModule({
  imports: [
  RouterModule.forChild(accountRoutes)
],
exports: [
  RouterModule
]
})

export class AccountRoutingModule { }





