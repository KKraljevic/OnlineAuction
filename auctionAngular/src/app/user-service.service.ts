import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';
import { jsonpCallbackContext } from '@angular/common/http/src/module';
import { Request } from 'selenium-webdriver/http';
import { Bid } from './bid';
import { map } from 'rxjs/operators';
import { Item } from './item';

 
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
    return this.http.post<User>(this.usersUrl+"/users", user);
  }

  public findUser(user: User) {
    return this.http.post<User>(this.usersUrl+"/login",user);
  }

  public findById(id :number): Observable<User> {
    return this.http.get<User>(this.usersUrl+"/users/"+id);  
  }

  public getBids(id: number): Observable<Bid[]> {
    return this.http.get<Bid[]>(this.usersUrl+"/users/"+id+"/bids");
  }
  
  public saveBid(bid: Bid,userId: number, itemId: number): Observable<Bid> {
    return this.http.post<Bid>(this.usersUrl+"/users/"+userId+"/bids/item/"+itemId, bid);
  }

  public getUsersItems(id: number) {
    return this.http.get<Item[]>(this.usersUrl+"/users/"+id+"/items");
  }
}
