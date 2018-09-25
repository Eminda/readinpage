import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-list-order',
    templateUrl: './list-order.component.html',
    styleUrls: ['./list-order.component.css']
})
export class ListOrderComponent implements OnInit {
    data;
    constructor(private http:HttpClient) {
    }

    ngOnInit() {
    }

    reload() {
        this.http.get("/api/scrape/retrieve").subscribe(data=>{
            console.log(data);
            this.data=data;
        })
    }

}
