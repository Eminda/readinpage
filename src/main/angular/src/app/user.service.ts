import { Injectable } from '@angular/core';

@Injectable()
export class UserService {
  isLoggedIn=false;
  constructor() { }

  getIsLoggedIn(){
    return this.isLoggedIn;
  }

  setIsLoggedIn(isLoggedIn){
    this.isLoggedIn=isLoggedIn;
  }
}
