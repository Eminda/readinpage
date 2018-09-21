import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {routing} from "./app.routing";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {SaveOrderComponent} from './save-order/save-order.component';
import {ListOrderComponent} from './list-order/list-order.component';

@NgModule({
    declarations: [
        AppComponent,
        SaveOrderComponent,
        ListOrderComponent
    ],
    imports: [
        BrowserModule,
        routing,
        ReactiveFormsModule,
        FormsModule,
        HttpClientModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
