import {Component, OnInit} from '@angular/core';
import {SETTING} from "../SETTING";

import {ToastrService} from "ngx-toastr";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {UserService} from "../user.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    user = {userName: "", password: ""}

    constructor(private http:HttpClient,private toaster:ToastrService,private router:Router,private userService:UserService) {
    }

    ngOnInit() {
    }

    login() {
        console.log(this.user);
        this.http.post(SETTING.HTTP + '/api/scrape/login', this.user).subscribe(data => {
            if (data === true) {
                this.userService.setIsLoggedIn(true);
                this.router.navigate(['main','save-order'])
                // this.clearFields();
            }else{
                this.toaster.error("Login Failed")
            }
        });
    }
}
