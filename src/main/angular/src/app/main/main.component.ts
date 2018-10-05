import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

    constructor(private userService: UserService, private router: Router) {
    }

    ngOnInit() {
        if (!this.userService.getIsLoggedIn()) {
            this.router.navigate(['login']);
        }
    }

    clearUser() {
      this.userService.setIsLoggedIn(false);
    }

}
