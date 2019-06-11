import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { Bid } from '../bid';
import { AuthenticationService } from '../authentication.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Item } from '../item';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-bids-list',
  templateUrl: './bids-list.component.html',
  styleUrls: ['./bids-list.component.css']
})
export class BidsListComponent implements OnInit {
    currentUser: User;
    bids: Bid[];
    items: Item[];

  constructor(private authenticationService: AuthenticationService,private router: Router, private route: ActivatedRoute) { 
    this.currentUser = new User();
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    this.bids=this.currentUser.bids;
    
  }

  ngOnInit() {
  }

}
