import { RouterModule, Routes } from '@angular/router';
import {SaveOrderComponent} from "./save-order/save-order.component";
import {ListOrderComponent} from "./list-order/list-order.component";
import {ViewListComponent} from "./view-list/view-list.component";
import {LoginComponent} from "./login/login.component";
import {MainComponent} from "./main/main.component";
import {UserChangeComponent} from "./user-change/user-change.component";


const routes: Routes = [
  { path: 'main', component: MainComponent, children:[
          {path:'save-order',component:SaveOrderComponent},
          {path:'list-order',component:ListOrderComponent},
          {path:'view-order',component:SaveOrderComponent},
          {path:'change-user',component:UserChangeComponent},
          { path: '**', redirectTo:'put-order' }
      ]},
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo:'login' }
  // { path: 'edit-user', component: EditUserComponent },
  // {path : '', component : LoginComponent}
];

export const routing = RouterModule.forRoot(routes,{useHash: true});
