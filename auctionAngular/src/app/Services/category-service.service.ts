import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Category } from '../Model/category';
import { Observable } from 'rxjs';
import { Item } from '../Model/item';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  
  springURL: string = "http://localhost:8080";
  herokuURL: string = "https://still-castle-19196.herokuapp.com";

  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.herokuURL+"/api/categories");
  }

  public findAllSubcategories(id: number): Observable<Category[]> {
    return this.http.get<Category[]>(this.herokuURL+"/api/categories/"+id+"/children");
  }

  public findCategoryById(id: number): Observable<Category>{
    return this.http.get<Category>(this.herokuURL+"/api/categories/"+id);
  }

  public findCategoryByName(name: string): Observable<Category>{
    return this.http.get<Category>(this.herokuURL+"/api/category/"+name);
  }

  public findCategoryItems(id: number, page: number, sort?: number) {
    let size = 9;
    let sortCriterias = ["id", "name", "currentPrice"];
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.append('size', size.toString());
    params = Number.isInteger(sort) ? params.append('sort', sortCriterias[sort]) : params;
    return this.http.get<Item[]>(this.herokuURL+"/api/categories/" + id + "/items", { params: params });
  }


}
