import { Component, OnInit, Input, TemplateRef, Output, EventEmitter } from '@angular/core';
import { Item } from 'src/app/Model/item';
import { User } from 'src/app/Model/user';
import { ItemService } from 'src/app/Services/item.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {

  @Input() currentUser: User;
  @Input() items: Item[] = [];

  @Output() deleteConfirmed = new EventEmitter();

  currentPage: number = 0;
  totalPages: number;

  modalRef: BsModalRef;
  msg: string;
  deleted:boolean=false;

  constructor(private itemService: ItemService, private modalService: BsModalService) {
  }

  ngOnInit() {
  }

  openModal(template: TemplateRef<any>) {
    this.msg="Are you sure you want to delete this item?";
    this.deleted=false;
    this.modalRef = this.modalService.show(template, { class: 'modal-sm' });
  }

  confirm(id: number): void {
    this.itemService.deleteItem(id).toPromise().then(resp => {
      console.log("deleted");
      this.deleteConfirmed.emit(true);
      this.modalRef.hide(),
      error => this.modalRef.setClass('is-invalid');
    }
    );
    this.msg="Successfully deleted!";
    this.deleted=true;
  }
  ok(){
    this.modalRef.hide();
  }
  decline(): void {
    this.modalRef.hide();
  }

}
