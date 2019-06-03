import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

 user: User;
 id: number;
 sub: Subscription;

  constructor(private userService: UserService,private router: Router, private route: ActivatedRoute) {
  this.user=new User();
   }

  ngOnInit() {
      this.route.paramMap.subscribe(params => {
      this.id = Number.parseInt(params.get("id"))
    });
    this.userService.findById(this.id).subscribe(data => {
    this.user= data});

  }
  

}



 
