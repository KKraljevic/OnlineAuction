import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  navTitle: string = "";
  sub: Subscription;
  currentSection: string;

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.sub = this.route.firstChild.url.subscribe(u => this.currentSection = u.toString());
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentSection = event.url.slice(9) + "";
        if(this.currentSection=="Newitem"){
          this.currentSection="New item";
        }
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
