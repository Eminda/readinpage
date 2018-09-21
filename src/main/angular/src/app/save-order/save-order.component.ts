import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-save-order',
    templateUrl: './save-order.component.html',
    styleUrls: ['./save-order.component.css']
})
export class SaveOrderComponent implements OnInit {

    constructor(private http:HttpClient) {
    }

    ngOnInit() {
    }

    submitOrder(order) {
        this.http.post('/scrape/submit',order).subscribe(data=>{
            console.log(data);
        })
    }
}
