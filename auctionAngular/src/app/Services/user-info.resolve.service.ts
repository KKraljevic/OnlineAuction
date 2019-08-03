import { Injectable } from '@angular/core';
import { Resolve, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { User } from '../Model/user';

@Injectable({
  providedIn: 'root'
})
export class UserInfo implements Resolve<any>{

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  resolve(route: ActivatedRouteSnapshot): Promise<User|boolean> {
    return this.authenticationService.currentUser.toPromise().then(user => {
      if (user) {
        return user;
      } else { 
        this.router.navigate(['/']);
        return false;
      }
    });
  }
}
