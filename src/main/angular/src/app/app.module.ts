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
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { UserChangeComponent } from './user-change/user-change.component';
import {UserService} from "./user.service";
@NgModule({
    declarations: [
        AppComponent,
        SaveOrderComponent,
        ListOrderComponent,
        ViewListComponent,
        LoginComponent,
        MainComponent,
        UserChangeComponent
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
    providers: [OrderService,UserService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
