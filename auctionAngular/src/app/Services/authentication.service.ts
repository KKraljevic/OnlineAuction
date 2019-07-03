import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../Model/user';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  private user: User;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<any>(JSON.parse(localStorage.getItem('currentUser') || 'null'));
    this.currentUser = this.currentUserSubject.asObservable();

    this.user = new User();
  }

  public get currentUserValue() {
    return this.currentUserSubject.value;
  }

  public login(email, password, remember) {
    this.user.email = email;
    this.user.password = password;
    console.log(remember);
    return this.http.post<User>('/api/login', this.user)
      .pipe(map(user => {
        if (remember)
          localStorage.setItem('currentUser', JSON.stringify(user));
        else {
          sessionStorage.setItem('currentUser', JSON.stringify(user));
        }
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    sessionStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
