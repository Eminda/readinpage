import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {OrderService} from "../order.service";
import {SETTING} from "../SETTING";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
    selector: 'app-view-list',
    templateUrl: './view-list.component.html',
    styleUrls: ['./view-list.component.css']
})
export class ViewListComponent implements OnInit {
    @ViewChild('downloadZipLink') private downloadZipLink: ElementRef;
    data;
    id;
    link;

    constructor(private orderService: OrderService,private http:HttpClient,private router:Router) {
        this.data = this.orderService.getData();
        this.id=this.orderService.getId();
        this.link=this.orderService.getLink();
        console.log(this.data);
    }

    ngOnInit() {
    }

    reload() {
        this.http.get(SETTING.HTTP+"/api/scrape/retrieve-company-list/"+this.id).subscribe(data=>{
            this.orderService.setData(data,this.id,this.link);
            this.data=data;
        })
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


}
