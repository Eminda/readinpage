import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {SETTING} from "../SETTING";

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
    retrieveEmailOnly=false;

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
        this.retrieveEmailOnly=false;
    }

    submitOrder(order) {
        this.http.post(SETTING.HTTP + '/api/scrape/submit', order).subscribe(data => {
            if (parseInt(""+data,10)>0) {
                this.toaster.success("Job was successfully submitted");
                // this.clearFields();
            }else{
                this.toaster.error("Only one job is allowed to work at once");
            }
        });
    }

    public changeListener(files: FileList) {
        console.log(files);
        if (files && files.length > 0) {
            let file: File = files.item(0);
            // console.log(file.name);
            // console.log(file.size);
            // console.log(file.type);
            let reader: FileReader = new FileReader();
            reader.readAsText(file);
            reader.onload = (e) => {
                let csv: string = reader.result;
                let d = csv.split('\n');
                let i = 0;
                this.urlList = '';
                d.forEach(url=> {
                    if (i != 0) {
                        this.urlList += url + ",";
                    }
                    i+=1;
                });
                if(this.urlList.length>1){
                    this.urlList=this.urlList.substring(0,this.urlList.length-1);
                }
            }
        }
    }
}
