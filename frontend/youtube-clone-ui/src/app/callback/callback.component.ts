import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";

//client will send a req to the auth server and the auth will res to the localhost4200/callback - 
//so when we initialize this callback comp, we re calling the spring backend and register the user to the daatabase
//once the registration is done, we will redirect the user to the home page.
@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) {
    this.userService.registerUser(); // this register() will make a call to the backend
    this.router.navigateByUrl(''); // this redirect to the home page
  }

  ngOnInit(): void {
  }

}
