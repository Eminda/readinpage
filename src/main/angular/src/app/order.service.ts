import { Injectable } from '@angular/core';
import {LocalStorage} from 'ngx-store';
@Injectable()
export class OrderService {
  @LocalStorage('DATA')
  dataSet=[];
  @LocalStorage('ID')
  id;
  @LocalStorage('LINK')
  link;
  constructor() { }

  setData(data,id,xlsLink){
    this.dataSet=data;
    this.id=id;
    this.link=xlsLink;
  }

  getData(){
    return this.dataSet;
  }

  getId(){
    return this.id;
  }

  getLink(){
    return this.link;
  }
}
