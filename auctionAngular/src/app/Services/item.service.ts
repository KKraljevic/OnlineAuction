import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../Model/item';
import { Bid } from '../Model/bid';
import { Country } from '../Model/country';
import { City } from '../Model/city';
import { User } from '../Model/user';
import { Location } from '../Model/location';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  springURL: string = "http://localhost:8080";
  herokuURL: string = "https://still-castle-19196.herokuapp.com";

  constructor(private http: HttpClient) {
  }

  public findAll(page?: number, sort?: number, userId?: number): Observable<Item[]> {
    let sortCriterias = ["id", "name", "currentPrice"];
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    params = Number.isInteger(sort) ? params.append('sort', sortCriterias[sort]) : params;
    params = Number.isInteger(userId) ? params.append('user', userId.toString()) : params;
    return this.http.get<Item[]>("/api/items", { params: params });
  }

  public findItems(search: string, page?: number, sort?: number, userId?: number): Observable<Item[]> {
    let sortCriterias = ["id", "name", "currentPrice"];
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    params = Number.isInteger(sort) ? params.append('sort', sortCriterias[sort]) : params;
    params = Number.isInteger(userId) ? params.append('user', userId.toString()) : params;
    params = params.append('search', search);
    return this.http.get<Item[]>("/api/search", { params: params });
  }

  public findFeaturedItems(userId?: number): Observable<Item[]> {
    let params = new HttpParams();
    params = Number.isInteger(userId) ? params.set('user', userId.toString()) : params;
    return this.http.get<Item[]>("/api/featuredItems", { params: params });
  }

  public findById(id: number): Observable<Item> {
    return this.http.get<Item>("/api/items/" + id);
  }

  public updateItem(itemId: number, newItem: Item): Observable<Item> {
    return this.http.put<Item>("/api/items/" + itemId, newItem);
  }

  public uploadImage(file: FormData): Observable<String> {
    return this.http.post<String>("/api/uploadImage", file, { responseType: 'text' as 'json' });
  }

  public deleteItem(itemId: number) {
    return this.http.delete("/api/items/" + itemId);
  }

  public getItemsBids(id: number, page?: number): Observable<Bid[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Bid[]>("/api/items/" + id + "/bids", { params: params });
  }

  public getHighestBidder(id: number): Observable<User> {
    return this.http.get<User>("/api/items/"+id+"/bids/highest");
  }

  public getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>("/api/countries");
  }

  public getCountryCities(iso3: string): Observable<City[]> {
    return this.http.get<City[]>("/api/countries/" + iso3);
  }

  public chargeCard(token: string, amount: number) {
    let headers = new HttpHeaders();
    headers=headers.set('token', token);
    headers=headers.append('amount',amount.toString());
    this.http.post('/payment/chargeCard', {}, { headers: headers })
      .subscribe(resp => {
        console.log(resp);
      })
  }

  public addPaymentAndShipping(token: string, amount: number,itemId: number, location: Location): Observable<Location>{
    let headers = new HttpHeaders();
    headers=headers.set('token', token);
    headers=headers.append('amount',amount.toString());
    return this.http.post<Location>("/api/items/"+itemId+"/payment",location,{headers: headers});
  }

}
