import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../Model/user';
import { jsonpCallbackContext } from '@angular/common/http/src/module';
import { Request } from 'selenium-webdriver/http';
import { Bid } from '../Model/bid';
import { map } from 'rxjs/operators';
import { Item } from '../Model/item';

 
@Injectable()
export class UserService {
  
  constructor(private http: HttpClient) {
  }
  
  public findAll(): Observable<User[]> {
    return this.http.get<User[]>("/api/users");
  }
 
  public save(user: User) {
    return this.http.post<User>("/api/users", user);
  }

  public findUser(user: User) {
    return this.http.post<User>("/api/login",user);
  }

  public findById(id :number): Observable<User> {
    return this.http.get<User>("/api/users/"+id);  
  }

  public getBids(id: number): Observable<Bid[]> {
    return this.http.get<Bid[]>("/api/users/"+id+"/bids");
  }
  
  public saveBid(bid: Bid,userId: number, itemId: number): Observable<Bid> {
    return this.http.post<Bid>("/api/users/"+userId+"/bids/item/"+itemId, bid);
  }

  public getUsersItems(id: number) {
    return this.http.get<Item[]>("/api/users/"+id+"/items");
  }
}
