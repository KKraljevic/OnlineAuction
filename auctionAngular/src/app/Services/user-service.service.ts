import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../Model/user';
import { jsonpCallbackContext } from '@angular/common/http/src/module';
import { Request } from 'selenium-webdriver/http';
import { Bid } from '../Model/bid';
import { map } from 'rxjs/operators';
import { Item } from '../Model/item';


@Injectable()
export class UserService {

  springURL: string = "http://localhost:8080";
  herokuURL: string = "https://still-castle-19196.herokuapp.com";

  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.herokuURL + "/api/users");
  }

  public save(user: User) {
    return this.http.post<User>(this.herokuURL + "/api/users", user);
  }

  public updateUser(user: User) {
    if (localStorage.getItem('currentUser')) {
      localStorage.setItem('currentUser', JSON.stringify(user));
    }
    else {
      sessionStorage.setItem('currentUser', JSON.stringify(user));
    }
    return this.http.put<User>(this.herokuURL + "/api/users/" + user.id, user);
  }

  public changePhoto(id: number, file: FormData): Observable<String> {
    return this.http.post<String>(this.herokuURL + "/api/users/" + id + "/photo", file, { responseType: 'text' as 'json' });
  }

  public findUser(user: User) {
    return this.http.post<User>(this.herokuURL + "/api/login", user);
  }

  public findById(id: number): Observable<User> {
    return this.http.get<User>(this.herokuURL + "/api/users/" + id);
  }

  public getBids(id: number, page?: number): Observable<Bid[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Bid[]>(this.herokuURL + "/api/users/" + id + "/bids", { params: params });
  }

  public saveBid(bid: Bid, userId: number, itemId: number): Observable<Bid> {
    return this.http.post<Bid>(this.herokuURL + "/api/users/" + userId + "/bids/item/" + itemId, bid);

  }

  public saveItem(item: Item, userId: number): Observable<Item> {
    return this.http.post<Item>(this.herokuURL + "/api/users/" + userId + "/items/", item);
  }

  public saveWishlist(userId: number, itemId: number): Observable<Item[]> {
    return this.http.post<Item[]>(this.herokuURL + "/api/users/" + userId + "/wishlist/" + itemId, "");
  }

  public getWishlist(userId: number, page?: number): Observable<Item[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Item[]>(this.herokuURL + "/api/users/" + userId + "/wishlist", { params: params });
  }

  public getAllWishlist(userId: number): Observable<Item[]> {
    return this.http.get<Item[]>(this.herokuURL + "/api/users/" + userId + "/allwishlist");
  }

  public getUsersItems(id: number, page?: number) {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Item[]>(this.herokuURL + "/api/users/" + id + "/items", { params: params });
  }

  public getAllUserItems(id: number) {
    return this.http.get<Item[]>(this.herokuURL + "/api/users/" + id + "/allitems");
  }

  public hasWishlist(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.herokuURL + "/api/users/" + userId + "/hasWishlist");

  }
  public hasBids(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.herokuURL + "/api/users/" + userId + "/hasBids");

  }
  public hasItems(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.herokuURL + "/api/users/" + userId + "/hasItems");

  }
}
