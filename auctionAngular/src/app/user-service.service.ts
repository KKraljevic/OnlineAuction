import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';
import { jsonpCallbackContext } from '@angular/common/http/src/module';
import { Request } from 'selenium-webdriver/http';
import { Bid } from './bid';
import { map } from 'rxjs/operators';

 
@Injectable()
export class UserService {
 
  private usersUrl: string;
 
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/api';
  }
  
  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl+"/users");
  }
 
  public save(user: User) {
    return this.http.post<User>(this.usersUrl+"/create", user);
  }

  public findUser(user: User) {
    return this.http.post<User>(this.usersUrl+"/login",user);
  }

  public findById(id :number): Observable<User> {
    return this.http.get<User>(this.usersUrl+"/profile/"+id);  
  }

  public getBids(id: number): Observable<Bid[]> {
    return this.http.get<Bid[]>(this.usersUrl+"/bids/"+id);
  }
  
  public saveBid(bid: Bid): Observable<Bid> {
    return this.http.post<Bid>(this.usersUrl+"/createBid", bid);
  }
  public deleteBid(b: Bid): Observable<any> {
    return this.http.delete(this.usersUrl+"/deleteBid"+b.bid_Id);
  }
}
