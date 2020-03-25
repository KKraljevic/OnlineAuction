import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from './category';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private springUrl: string;
  constructor(private http: HttpClient) {
    this.springUrl = 'http://localhost:8080';
  }

  public findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.springUrl+"/categories");
  }
  public findAllSubcategories(id: number): Observable<Category[]> {
    return this.http.get<Category[]>(this.springUrl+"/categories/"+id+"/children");
  }

}
