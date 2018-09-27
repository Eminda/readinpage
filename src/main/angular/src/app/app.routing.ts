import { RouterModule, Routes } from '@angular/router';
import {SaveOrderComponent} from "./save-order/save-order.component";
import {ListOrderComponent} from "./list-order/list-order.component";
import {ViewListComponent} from "./view-list/view-list.component";


const routes: Routes = [
  { path: 'put-order', component: SaveOrderComponent },
  { path: 'list-order', component: ListOrderComponent },
  { path: 'view-order', component: ViewListComponent },
  { path: '**', redirectTo:'put-order' }
  // { path: 'edit-user', component: EditUserComponent },
  // {path : '', component : LoginComponent}
];

export const routing = RouterModule.forRoot(routes,{useHash: true});
