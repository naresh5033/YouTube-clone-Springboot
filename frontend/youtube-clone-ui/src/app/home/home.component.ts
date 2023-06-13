import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) {
    this.router.navigateByUrl('/featured'); // so whenever we re in the home page, it will navigate to the feature comp.
  }

  ngOnInit(): void {
  }

}
