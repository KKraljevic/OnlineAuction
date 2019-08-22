import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../Model/user';
import { Bid } from '../Model/bid';
import { Item } from '../Model/item';
import { Rating } from '../Model/rating';


@Injectable()
export class UserService {

  springURL: string = "http://localhost:8080";
  herokuURL: string = "https://still-castle-19196.herokuapp.com";

  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.springURL + "/api/users");
  }

  public save(user: User) {
    return this.http.post<User>(this.springURL + "/api/users", user);
  }

  public updateUser(user: User) {
    if (localStorage.getItem('currentUser')) {
      localStorage.setItem('currentUser', JSON.stringify(user));
    }
    else {
      sessionStorage.setItem('currentUser', JSON.stringify(user));
    }
    return this.http.put<User>(this.springURL + "/api/users/" + user.id, user);
  }

  public changePhoto(id: number, file: FormData): Observable<String> {
    return this.http.post<String>(this.springURL + "/api/users/" + id + "/photo", file, { responseType: 'text' as 'json' });
  }

  public findUser(user: User) {
    return this.http.post<User>(this.springURL + "/api/login", user);
  }

  public findById(id: number): Observable<User> {
    return this.http.get<User>(this.springURL + "/api/users/" + id);
  }

  public getBids(id: number, page?: number, type?: number): Observable<Bid[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    type = type != undefined ? type : 0;
    console.log("Service:" + type);
    switch (type) {
      case 0:
        return this.http.get<Bid[]>(this.springURL + "/api/users/" + id + "/bids/active", { params: params });
      case 1:
        return this.http.get<Bid[]>(this.springURL + "/api/users/" + id + "/bids/lost", { params: params });
      case 2:
        return this.http.get<Bid[]>(this.springURL + "/api/users/" + id + "/bids/won", { params: params });
      default: break;
    }
  }

  public countWonBids(id: number): Observable<String> {
    return this.http.get<String>(this.springURL + "/api/users/" + id + "/bids/won/count", { responseType: 'text' as 'json' });
  }

  public countPendingItems(id: number): Observable<String> {
    return this.http.get<String>(this.springURL + "/api/users/" + id + "/items/expired/count", { responseType: 'text' as 'json' });
  }

  public saveBid(bid: Bid, userId: number, itemId: number): Observable<Bid> {
    return this.http.post<Bid>(this.springURL + "/api/users/" + userId + "/bids/item/" + itemId, bid);
  }

  public saveItem(item: Item, userId: number): Observable<Item> {
    return this.http.post<Item>(this.springURL + "/api/users/" + userId + "/items/" + item.category.id, item);
  }

  public saveWishlist(userId: number, itemId: number): Observable<Item[]> {
    return this.http.post<Item[]>(this.springURL + "/api/users/" + userId + "/wishlist/" + itemId, "");
  }

  public getWishlist(userId: number, page?: number): Observable<Item[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Item[]>(this.springURL + "/api/users/" + userId + "/wishlist", { params: params });
  }

  public getAllWishlist(userId: number): Observable<Item[]> {
    return this.http.get<Item[]>(this.springURL + "/api/users/" + userId + "/allwishlist");
  }

  public getItems(id: number, page?: number, type?: number) {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    type = type != undefined ? type : 0;
    switch (type) {
      case 0:
        return this.http.get<Item[]>(this.springURL + "/api/users/" + id + "/items/active", { params: params });
      case 1:
        return this.http.get<Item[]>(this.springURL + "/api/users/" + id + "/items/pending", { params: params });
      case 2:
        return this.http.get<Item[]>(this.springURL + "/api/users/" + id + "/items/sold", { params: params });
      case 3:
        return this.http.get<Item[]>(this.springURL + "/api/users/" + id + "/items/expired", { params: params });
      default:
        break;
    }
  }

  public getAllUserItems(id: number) {
    return this.http.get<Item[]>(this.springURL + "/api/users/" + id + "/allitems");
  }

  public addUserRating(id: number, rating: Rating) {
    return this.http.post<Rating>(this.springURL + "/api/users/" + id + "/rating", rating);
  }

  public getUserRating(id: number) {
    return this.http.get(this.springURL + "/api/users/" + id + "/rating", { responseType: 'text' as 'json' });
  }

  public hasWishlist(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.springURL + "/api/users/" + userId + "/hasWishlist");

  }
  public hasBids(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.springURL + "/api/users/" + userId + "/hasBids");

  }
  public hasItems(userId: number): Observable<boolean> {
    return this.http.get<boolean>(this.springURL + "/api/users/" + userId + "/hasItems");

  }
}
