import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {

  currentUser: User;
  id: number;
  sub: Subscription;

  constructor(private authenticationService: AuthenticationService, private router: Router, private route: ActivatedRoute) {
    this.currentUser = new User();
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

}




