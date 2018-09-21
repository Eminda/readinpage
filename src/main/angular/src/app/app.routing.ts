import { RouterModule, Routes } from '@angular/router';
import {SaveOrderComponent} from "./save-order/save-order.component";
import {ListOrderComponent} from "./list-order/list-order.component";


const routes: Routes = [
  { path: 'put-order', component: SaveOrderComponent },
  { path: 'list-order', component: ListOrderComponent },
  { path: '**', redirectTo:'put-order' }
  // { path: 'edit-user', component: EditUserComponent },
  // {path : '', component : LoginComponent}
];

export const routing = RouterModule.forRoot(routes);
