import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './Services/authentication.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    num: number;
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authenticationService.currentUserValue;

        if (currentUser && !route.url.toString().includes("registration", this.num)) {
            return true;
        }
        else if (!currentUser && route.url.toString().includes("registration", this.num)) {
            return true;
        }
        else {
            this.router.navigate(['/'], { queryParams: { returnUrl: state.url } });
            return false;
        }

    }
}
