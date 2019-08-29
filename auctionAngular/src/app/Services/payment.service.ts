import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  
  springURL: string = "http://localhost:8080";
  herokuURL: string = "https://still-castle-19196.herokuapp.com";

  constructor(private http: HttpClient) { }

  public chargeCard(token: string) {
    let headers = new HttpHeaders({ 'amount': '100', 'token': token });
    this.http.post('/payment/chargeCard', {}, { headers: headers })
      .subscribe(resp => {
        console.log(resp);
      })
  }

  public createCustomer(token: string, email: string) {
    let headers = new HttpHeaders({ 'email': email, 'token': token });
    return this.http.post('/payment/createCustomer', {}, { headers: headers }); 
  }
  
}
