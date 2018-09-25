import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-save-order',
    templateUrl: './save-order.component.html',
    styleUrls: ['./save-order.component.css']
})
export class SaveOrderComponent implements OnInit {
    email;
    password;
    jobName;
    description;
    urlList;
    filterList;

    constructor(private http: HttpClient, private toaster: ToastrService) {
        this.clearFields();
    }

    ngOnInit() {
    }

    clearFields() {
        this.email = '';
        this.password = '';
        this.jobName = '';
        this.description = '';
        this.urlList = '';
        this.filterList = '';
    }

    submitOrder(order) {
        this.http.post('/api/scrape/submit', order).subscribe(data => {
            if (data === true) {
                this.toaster.success("Job was successfully submitted");
                // this.clearFields();
            }
        });
    }
}
