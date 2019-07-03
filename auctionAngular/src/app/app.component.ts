import { Component, NgModule } from '@angular/core';
import { User } from './Model/user';
import { AuthenticationService } from './Services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {

  currentUser: User;

  constructor(private router: Router,private authenticationService: AuthenticationService) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
}

logout() {
    this.authenticationService.logout();
    this.router.navigate(['/']);
}
 
}
