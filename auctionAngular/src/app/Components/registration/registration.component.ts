import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { UserService } from '../../Services/user-service.service';
import { User } from '../../Model/user';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/Services/authentication.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  submitted = false;
  errorMessage: String;
  user: User;

  constructor(private router: Router, private formBuilder: FormBuilder, private userService: UserService,
    private authService: AuthenticationService) {
    this.user = new User();
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }
    else {
      this.user.firstName = this.registerForm.get('firstName').value;
      this.user.lastName = this.registerForm.get('lastName').value;
      this.user.email = this.registerForm.get('email').value;
      this.user.password = this.registerForm.get('password').value;
      this.user.photo = "https://purecremation.azurewebsites.net/Content/images/profile-placeholder.png";
      this.userService.save(this.user).subscribe(
        r => {
          this.user.id = r.id;
        },
        error => this.errorMessage = "Account with this email already exicts!"
      );
      this.router.navigate(['/login']);
    }
  }

  gotoUserProfile(id) {
    this.router.navigate(['/account/profile/', id]);
  }

}
