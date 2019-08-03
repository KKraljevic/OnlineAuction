import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountRoutingModule } from './account-routing.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ItemsComponent } from './items/items.component';
import { BidsComponent } from './bids/bids.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { NewItemComponent } from './new-item/new-item.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FileUploadModule } from 'ng2-file-upload';
import { ComboDatepickerModule } from 'ngx-combo-datepicker';

@NgModule({
  declarations: [
    UserProfileComponent,
    ItemsComponent,
    NewItemComponent,
    BidsComponent,
    WishlistComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FileUploadModule,
    AccountRoutingModule,
    NgbModule,
    ComboDatepickerModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ]
})
export class AccountModule { }
