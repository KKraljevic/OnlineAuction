import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user-service.service';
import { Observable } from 'rxjs';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit{
 
  
 @Input() user: User;
  loginForm: FormGroup;
  submitted = false;
  
  constructor(private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder, private userService: UserService) {
  this.user=new User();
  }
  
  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  get f() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted=true;
    
    if (this.loginForm.invalid) {
      return;
    }
    else {
      this.user.email=this.loginForm.get('email').value;
      this.user.password=this.loginForm.get('password').value;

      this.userService.findUser(this.user).subscribe(
        r => {
            if (r!=null)
              {
                this.gotoUserProfile(r.id);
              }
            else
              alert("Incorrect email or password!");
        });
    }
  }
 
  gotoUserProfile(id){
      this.router.navigate(['/profile/', id]);
  }
  gotoUserList() {
    this.router.navigate(['/users']);
  }

}
