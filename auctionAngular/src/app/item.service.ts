import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from './item';
import { Bid } from './bid';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private springUrl: string;
  
  constructor(private http: HttpClient) {
    this.springUrl = 'http://localhost:8080/api';
  }
  
  public findAll(): Observable<Item[]> {
    return this.http.get<Item[]>(this.springUrl+"/items");
  }

  public findFeaturedItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.springUrl+"/featuredItems");
  }

  public findById(id: number): Observable<Item> {
    return this.http.get<Item>(this.springUrl+"/items/"+id);
  }

  public updateItem(itemId: number,newItem : Item): Observable<Item> {
    return this.http.put<Item>(this.springUrl+"/items/"+itemId,newItem);
  }

  public getItemsBids(id: number): Observable<Bid[]> {
    return this.http.get<Bid[]>(this.springUrl+"/items/"+id+"/bids");
  }
}
