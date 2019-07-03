import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../Model/item';
import { Bid } from '../Model/bid';

@Injectable({
  providedIn: 'root'
})
export class ItemService {


  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<Item[]> {
    return this.http.get<Item[]>("/api/items");
  }

  public findFeaturedItems(): Observable<Item[]> {
    return this.http.get<Item[]>("/api/featuredItems");
  }

  public findCategoryItems(id: number, page: number,sort?: number) {
    let size = 3;
    let params;
    let sortCriterias=["id","name","currentPrice"];
    if(sort){
      params = new HttpParams().set('page', page.toString()).set('size',size.toString()).set('sort',sortCriterias[sort]);
    }
    else {
      params = new HttpParams().set('page', page.toString()).set('size',size.toString());
    }
    return this.http.get<Item[]>("/api/categories/" + id + "/items", { params: params });
  }

  public findById(id: number): Observable<Item> {
    return this.http.get<Item>("/api/items/" + id);
  }

  public updateItem(itemId: number, newItem: Item): Observable<Item> {
    return this.http.put<Item>("/api/items/" + itemId, newItem);
  }

  public getItemsBids(id: number): Observable<Bid[]> {
    return this.http.get<Bid[]>("/api/items/" + id + "/bids");
  }
}
