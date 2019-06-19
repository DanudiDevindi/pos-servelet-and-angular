import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {customerdto} from "../../dtos/customerDto";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  readonly baseUrl = environment.apiUrl + '/customers';
  readonly baseUrl1 = environment.apiUrl + '/customers?id=';

  constructor(private  http: HttpClient) {

  }
  getAllCustomers(): Observable<customerdto[]>{
    return this.http.get<customerdto[]>('http://localhost:8080/pos/customers');
  }

  saveCustomer(customer: customerdto): Observable<boolean> {
    console.log(customer);
    return this.http.post<boolean>(this.baseUrl, customer);
  }

  searchCustomer(id: String): Observable<customerdto> {
    return this.http.get<customerdto>(this.baseUrl+ id);
  }

  deleteCustomer(id: string): Observable<boolean> {
    return this.http.delete<boolean>(this.baseUrl1+ id);
  }

}
