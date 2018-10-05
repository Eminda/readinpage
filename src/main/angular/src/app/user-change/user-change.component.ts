import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SETTING} from "../SETTING";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-user-change',
  templateUrl: './user-change.component.html',
  styleUrls: ['./user-change.component.css']
})
export class UserChangeComponent implements OnInit {
  user={
      userName:'admin',
      password:"",
      newPassword:""
  };
  repeatPass;
  constructor(private http:HttpClient,private toaster:ToastrService) { }

  ngOnInit() {
  }

  changePassword(){
      if(this.user.newPassword==this.repeatPass) {
          this.http.post(SETTING.HTTP + '/api/scrape/update-user', this.user).subscribe(data => {
              if (data === true) {
                  this.toaster.success("Password changed");
                  this.user.password="";
                  this.user.newPassword="";
                  this.repeatPass="";
              } else {
                  this.toaster.error("Password change Failed")
              }
          });
      }else{
        this.toaster.warning("New password and repeat password are not the same");
      }
  }

}
