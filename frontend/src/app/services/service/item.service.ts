import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {customerdto} from "../../dtos/customerDto";
import {Observable} from "rxjs/internal/Observable";
import {HttpClient} from "@angular/common/http";
import {itemdto} from "../../dtos/itemDto";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  readonly baseUrl = environment.apiUrl + '/items';
  readonly baseUrl1 = environment.apiUrl + '/items?code=';

  constructor(private http: HttpClient) {

  }

  getAllItems(): Observable<itemdto[]>{
    return this.http.get<itemdto[]>('http://localhost:8080/pos/items');
  }

  saveItems(item: itemdto): Observable<boolean> {
    console.log(item);
    return this.http.post<boolean>(this.baseUrl, item);
  }

  deleteItem(code: string): Observable<boolean> {
    return this.http.delete<boolean>(this.baseUrl1+ code);
  }
}
