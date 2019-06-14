import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../authentication.service';
import { Bid } from '../bid';
import { UserService } from '../user-service.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{

  currentUser: User;
  bids: Bid[]=[];
  id: number;

  constructor(private authenticationService: AuthenticationService, private userService: UserService, private router: Router, private route: ActivatedRoute) {
    this.currentUser = new User();
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser=x;
    });
  }

  ngOnInit(): void {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser=x;
    });
    this.userService.getBids(this.currentUser.id).subscribe(b=>this.bids=b);
  }

}




