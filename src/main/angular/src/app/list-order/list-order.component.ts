import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SETTING} from "../SETTING";
import {OrderService} from "../order.service";
import {Router} from "@angular/router";
import {ResponseContentType} from "@angular/http";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-list-order',
    templateUrl: './list-order.component.html',
    styleUrls: ['./list-order.component.css']
})
export class ListOrderComponent implements OnInit {


    @ViewChild('downloadZipLink') private downloadZipLink: ElementRef;
    data;
    stopped=-1;

    constructor(private http: HttpClient, private orderService: OrderService, private router: Router,private toaster:ToastrService) {
        this.http.get(SETTING.HTTP + "/api/scrape/retrieve").subscribe(data => {
            console.log(data);
            this.data = data;
        })
    }

    ngOnInit() {
    }

    reload() {
        this.http.get(SETTING.HTTP + "/api/scrape/retrieve").subscribe(data => {
            console.log(data);
            this.data = data;
        })
    }

    viewList(id, xlsLink) {
        this.http.get(SETTING.HTTP + "/api/scrape/retrieve-company-list/" + id).subscribe(data => {
            this.orderService.setData(data, id, xlsLink);
            this.router.navigate(['main','view-order']);
        })
    }

    getShortList(list) {
        if (list.length > 20) {
            return list.substring(0, 20) + "...";
        }
        return list;
    }


    public async downloadResource(id: string): Promise<Blob> {
        const file =  await this.http.get<Blob>(
            SETTING.HTTP + "/api/scrape/retrieve-xls/" + id + "_scape.xlsx",
            {responseType: 'blob' as 'json'}).toPromise();
        return file;
    }
    public async download(jobID) {
        const blob = await this.downloadResource(jobID);
        const url = window.URL.createObjectURL(blob);

        const link = this.downloadZipLink.nativeElement;
        link.href = url;
        link.download = jobID+'_scrape.xlsx';
        link.click();

        window.URL.revokeObjectURL(url);
    }

    stop(jobID){
        this.http.get(SETTING.HTTP + '/api/scrape/stop').subscribe(data => {
            if (data==true) {
                this.toaster.success("Job was successfully stopped");
                this.stopped=jobID;
            }else{
                this.toaster.error("Stopping failed");
            }
        });
    }

}
