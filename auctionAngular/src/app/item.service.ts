import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from './item';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private springUrl: string;
  constructor(private http: HttpClient) {
    this.springUrl = 'http://localhost:8080';
  }
  
  public findAll(): Observable<Item[]> {
    return this.http.get<Item[]>(this.springUrl+"/items");
  }

  public findById(id: number): Observable<Item> {
    return this.http.get<Item>(this.springUrl+"/item/"+id);
  }
}
