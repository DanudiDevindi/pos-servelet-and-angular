import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

import {NgForm} from "@angular/forms";
import {itemdto} from "../../dtos/itemDto";
import {ItemService} from "../../services/service/item.service";
import {customerdto} from "../../dtos/customerDto";

@Component({
  selector: 'app-manage-items',
  templateUrl: './manage-items.component.html',
  styleUrls: ['./manage-items.component.css']
})
export class ManageItemsComponent implements OnInit {

  ite: itemdto[] =[];
  selectedItem: itemdto = new itemdto('','','',0);
  @ViewChild('txtcode') txtcode: ElementRef;
  @ViewChild('frmItem') frmItem:NgForm;

  constructor(private itemservice : ItemService) { }

  ngOnInit() {

  }

  getAllItems(){
    this.itemservice.getAllItems().subscribe(i =>{
      this.ite = i;
    });
    console.log(this.ite);
  }

  saveItems(): void {

    if (!this.frmItem.invalid) {

      this.itemservice.saveItems(this.selectedItem)
        .subscribe(resp => {
          if (resp) {
            alert('Item has been saved successfully');
            this.ite.push(this.selectedItem);
          } else {
            alert('Failed to save the item');
          }
        });

    } else {
      alert('Invalid Data, Please Correct...!');
    }
  }

  tableRow_Click_Item(item: itemdto): void {
    console.log(item);
    this.selectedItem = Object.assign({}, item);
  }

  deleteItem(code): void {
    console.log(code);
    if (confirm('Are you sure you want to delete this item?')) {
      this.itemservice.deleteItem(code).subscribe(
        (result) => {
          if (result) {
            alert('Failed to delete the item');

            this.getAllItems();
          } else {
            alert('Item has been deleted successfully');
          }
          this.getAllItems();
        }
      );
    }
  }

  updateItems(): void {

    if (!this.frmItem.invalid) {

      this.itemservice.updateItems(this.selectedItem)
        .subscribe(resp => {
          if (!resp) {
            alert('Item has been updated successfully');
            this.ite.push(this.selectedItem);
          } else {
            alert('Failed to updated the item');
          }
        });

    } else {
      alert('Invalid Data, Please Correct...!');
    }
  }

}
