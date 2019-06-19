import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {customerdto} from "../../dtos/customerDto";
import {NgForm} from "@angular/forms";
import {CustomerService} from "../../services/service/customer.service";

@Component({
  selector: 'app-manage-customers',
  templateUrl: './manage-customers.component.html',
  styleUrls: ['./manage-customers.component.css']
})
export class ManageCustomersComponent implements OnInit {

  cus: customerdto[] =[];
  selectedCustomer: customerdto = new customerdto('', '', '');
   @ViewChild('txtId') txtId: ElementRef;
   @ViewChild('frmCustomer') frmCustomer:NgForm;

  constructor(private customerservice : CustomerService) { }


  ngOnInit() {
    // this.customerservice.getAllCustomers().subscribe(customers =>{
    //   this.customers = customers;

    // }
    this.getAllCustomers();
  }
  getAllCustomers(){
    this.customerservice.getAllCustomers().subscribe(cust =>{
      this.cus = cust;
    });
    console.log(this.cus);
  }
  addCustomer(id,name,address) {
    if (name === '' || address) {
      alert('Please input values');
      return;
    }
  }

  saveCustomer(): void {
    if (!this.frmCustomer.invalid) {

      this.customerservice.saveCustomer(this.selectedCustomer)
        .subscribe(resp => {
          if (resp) {
            alert('Customer has been saved successfully');
            this.cus.push(this.selectedCustomer);
          } else {
            alert('Failed to save the customer');
          }
        });

    } else {
      alert('Invalid Data, Please Correct...!');
    }
  }

  tableRow_Click(customer: customerdto): void {
  console.log(customer);
    this.selectedCustomer = Object.assign({}, customer);
  }


  deleteCustomer(id): void {
    console.log(id);
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerservice.deleteCustomer(id).subscribe(
        (result) => {
          if (result) {
            alert('Customer has been deleted successfully');
            this.getAllCustomers();
          } else {
            alert('Failed to delete the customer');
          }
          this.getAllCustomers();
        }
      );
    }
  }
}
