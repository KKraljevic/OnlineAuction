import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule, Routes } from '@angular/router';
import { UsersListComponent } from './users-list/users-list.component';
import { AppComponent } from './app.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { RegistrationComponent } from './registration/registration.component';

const routes: Routes = [
  { path: '', component: AppComponent},
  { path: 'users', component: UsersListComponent },
  { path: 'login', component: LoginFormComponent },
  { path: 'profile', component: UserProfileComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'profile/:id', component: UserProfileComponent}
];

  @NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
export class AppRoutingModule { }




 
