import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {routing} from "./app.routing";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {SaveOrderComponent} from './save-order/save-order.component';
import {ListOrderComponent} from './list-order/list-order.component';
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { ViewListComponent } from './view-list/view-list.component';
import {OrderService} from "./order.service";
import {WebStorageModule} from 'ngx-store';
@NgModule({
    declarations: [
        AppComponent,
        SaveOrderComponent,
        ListOrderComponent,
        ViewListComponent
    ],
    imports: [
        BrowserModule,
        routing,
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        HttpClientModule,
        WebStorageModule
    ],
    providers: [OrderService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
