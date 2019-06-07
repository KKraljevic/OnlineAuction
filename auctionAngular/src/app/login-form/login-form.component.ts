import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user-service.service';
import { Observable } from 'rxjs';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { AuthenticationService } from '../authentication.service';
import { first } from 'rxjs/operators';


@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {


  user: User;
  loginForm: FormGroup;
  submitted = false;
  returnUrl: string;
  error: string;

  constructor(private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder, private userService: UserService, private authService: AuthenticationService) {
    this.user = new User();
    if (this.authService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      remember: []
    });
  }

  get f() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }
    this.authService.login(this.f.email.value, this.f.password.value,this.f.remember.value)
      .pipe(first())
      .subscribe(
        data => {
          if (data != null) {
            this.router.navigate(['/']);
          }
          else
            alert("Incorrect email or password!");
        });


  }

  gotoUserProfile(id) {
    this.router.navigate(['/profile/', id]);
  }
  gotoUserList() {
    this.router.navigate(['/users']);
  }

}
