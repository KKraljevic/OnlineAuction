import { Component, OnInit, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute, Event, NavigationEnd, NavigationStart } from '@angular/router';
import { User } from '../../Model/user';
import { AuthenticationService } from '../../Services/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.css']
})

export class AppHeaderComponent {
   
  
  currentUser: User;
  path: any;

  constructor(private router: Router, private authenticationService: AuthenticationService) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    this.router.events.subscribe((event:Event) => {
      if(event instanceof NavigationEnd ){
        this.path=event.url;
      }
  });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
  
}
