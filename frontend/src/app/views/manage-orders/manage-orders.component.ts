import { Component, OnInit } from '@angular/core';
import {customerdto} from "../../dtos/customerDto";
import {CustomerService} from "../../services/service/customer.service";
import {itemdto} from "../../dtos/itemDto";
import {ItemService} from "../../services/service/item.service";

@Component({
  selector: 'app-manage-orders',
  templateUrl: './manage-orders.component.html',
  styleUrls: ['./manage-orders.component.css']
})
export class ManageOrdersComponent implements OnInit {
  cus: customerdto[] =[];
  selectedCustomer: customerdto = new customerdto('', '', '');
  selectedCustomer1: customerdto = new customerdto('', '', '');
  ite: itemdto[] =[];
  selectedItem: itemdto = new itemdto('','','',0);
  selectedItem1: itemdto = new itemdto('','','',0);
  constructor(private customerservice: CustomerService, private  itemservice: ItemService) { }

  ngOnInit() {
    this.getAllCustomers();
    this.getAllItems();

  }

  getAllCustomers(){
    this.customerservice.getAllCustomers().subscribe(cust =>{
      this.cus = cust;
    });
    console.log(this.cus);
  }


  searchCustomer(event: any): void{
    for (this.selectedCustomer of this.cus){
      if(this.selectedCustomer.id===event.target.value){
        this.selectedCustomer1=this.selectedCustomer;
      }
    }
  }

  getAllItems(){
    this.itemservice.getAllItems().subscribe(i =>{
      this.ite = i;
    });
    console.log(this.ite);
  }
  searchItem(event: any): void{
    for(this.selectedItem of this.ite){
      if(this.selectedItem.code===event.target.value){
        this.selectedItem1= this.selectedItem;
      }
    }
  }
}


