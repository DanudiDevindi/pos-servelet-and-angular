import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';


import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

import {RouterModule, Routes} from "@angular/router";
import { ManageCustomersComponent } from './views/manage-customers/manage-customers.component';
import { ManageItemsComponent } from './views/manage-items/manage-items.component';
import { ManageOrdersComponent } from './views/manage-orders/manage-orders.component';
import { MainComponent } from './views/main/main.component';
import { DashboardComponent } from './views/dashboard/dashboard.component';

const  routes: Routes =[
  {
    path: 'customer',
    component: ManageCustomersComponent
  },
  {
    path : 'item',
    component: ManageItemsComponent
  },
  {
    path : 'order',
    component: ManageOrdersComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    ManageCustomersComponent,
    ManageItemsComponent,
    ManageOrdersComponent,
    MainComponent,
    DashboardComponent,

  ],
  imports: [
   
    BrowserModule,
    HttpClientModule,
    FormsModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
