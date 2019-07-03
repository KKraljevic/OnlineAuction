import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../Model/category';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  
  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<Category[]> {
    return this.http.get<Category[]>("/api/categories");
  }
  public findAllSubcategories(id: number): Observable<Category[]> {
    return this.http.get<Category[]>("/api/categories/"+id+"/children");
  }

}
