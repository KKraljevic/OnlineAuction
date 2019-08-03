import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../Model/item';
import { Bid } from '../Model/bid';
import { Country } from '../Model/country';
import { City } from '../Model/city';
import { FileItem } from 'ng2-file-upload';

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
    return this.http.get<Item[]>(this.herokuURL+"/api/items", { params: params });
  }
  
  public findItems(search: string, page?: number, sort?: number, userId?: number): Observable<Item[]> {
    let sortCriterias = ["id", "name", "currentPrice"];
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    params = Number.isInteger(sort) ? params.append('sort', sortCriterias[sort]) : params;
    params = Number.isInteger(userId) ? params.append('user', userId.toString()) : params;
    params = params.append('search',search);
    return this.http.get<Item[]>(this.herokuURL+"/api/search", { params: params });
  }

  public findFeaturedItems(userId?: number): Observable<Item[]> {
    let params = new HttpParams();
    params = Number.isInteger(userId) ? params.set('user', userId.toString()) : params;
    return this.http.get<Item[]>(this.herokuURL+"/api/featuredItems", { params: params });
  }

  public findById(id: number): Observable<Item> {
    return this.http.get<Item>(this.herokuURL+"/api/items/" + id);
  }

  public updateItem(itemId: number, newItem: Item): Observable<Item> {
    return this.http.put<Item>(this.herokuURL+"/api/items/" + itemId, newItem);
  }

  public uploadImage(file: FormData) : Observable<String> {
    return this.http.post<String>(this.herokuURL+"/api/uploadImage",file,{responseType: 'text' as 'json'});
  }

  public deleteItem(itemId: number, newItem: Item) {
    return this.http.delete(this.herokuURL+"/api/items/" + itemId);
  }

  public getItemsBids(id: number,page?:number): Observable<Bid[]> {
    let params = new HttpParams();
    params = Number.isInteger(page) ? params.append('page', page.toString()) : params;
    return this.http.get<Bid[]>(this.herokuURL+"/api/items/" + id + "/bids", {params:params});
  }

  public getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(this.herokuURL+"/api/countries");
  }

  public getCountryCities(iso3: string): Observable<City[]> {
    return this.http.get<City[]>(this.herokuURL+"/api/countries/"+iso3);
  }
}
