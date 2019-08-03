import { Component, OnInit, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute, Event, NavigationEnd, NavigationStart } from '@angular/router';
import { User } from '../../Model/user';
import { AuthenticationService } from '../../Services/authentication.service';
import { UserService } from 'src/app/Services/user-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.css']
})

export class AppHeaderComponent implements OnInit {

  currentUser: User;
  hasWishlist: boolean;
  hasBids: boolean;
  hasItems: boolean;
  path: any;

  searchForm: FormGroup;
  submitted: boolean = false;

  constructor(private router: Router, private authenticationService: AuthenticationService, private userService: UserService,
    private fb: FormBuilder) {

    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.path = event.url;
      }
    });
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;
      console.log(this.currentUser);
      if (this.currentUser != null) {
        this.userService.hasBids(user.id).subscribe(res => this.hasBids = res);
        this.userService.hasItems(user.id).subscribe(res => this.hasItems = res);
        this.userService.hasWishlist(user.id).subscribe(res => this.hasWishlist = res);
      }
    });
    this.searchForm = this.fb.group({
      search: ['']
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  searchItems() {
    this.submitted = true;

    if (this.searchForm.invalid) {
      return;
    }

    this.router.navigate(['/search/', this.searchForm.get('search').value]);
  }

}
