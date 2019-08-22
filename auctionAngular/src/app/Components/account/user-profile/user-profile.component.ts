import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { User } from '../../../Model/user';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../../../Services/authentication.service';
import { UserService } from '../../../Services/user-service.service';
import { Image } from '../../../Model/image';
import { FormControl, Validators, FormBuilder, FormGroup, ValidationErrors } from '@angular/forms';
import { FileUploader, FileLikeObject } from 'ng2-file-upload';
import { ItemService } from 'src/app/Services/item.service';
import { SafeUrl, DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  currentUser: User;

  editMode: boolean = false;
  editUserForm: FormGroup;
  form: FormGroup;
  submitted: boolean = false;
  msgSaved: boolean;
  now: Date = new Date();

  @ViewChild('profilePhoto', { static: true }) fileInput: ElementRef;
  uploader: FileUploader;
  errorMessage: string;

  gender: string;
  userPhoto: any;

  constructor(private authenticationService: AuthenticationService, private userService: UserService,
    private itemService: ItemService, private formBuilder: FormBuilder, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {

    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      if (x != null) {
        this.gender = this.GenderFullName(x.gender);
        this.userPhoto = x.photo;
      }

    });

    this.editUserForm = this.formBuilder.group({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      phone: new FormControl('', [Validators.required, Validators.pattern('^([^a-zA-Z]*)$')]),
      birthDate: new FormControl('', Validators.required),
      gender: new FormControl('', Validators.required)
    })
    if (this.editMode) {
      this.editUserForm.enable();
    }
    else {
      this.editUserForm.disable();
    }
    if (this.currentUser.birthDate)
      this.f.birthDate.setValue(new Date(this.currentUser.birthDate));
    else
      this.f.birthDate.setValue('');

    const headers = [{ name: 'Accept', value: 'application/json' }];
    this.uploader = new FileUploader({
      url: '/api/uploadImage', autoUpload: false, headers: headers,
      allowedMimeType: ['image/jpeg', 'image/png'], maxFileSize: 1048000
    });
    this.uploader.onAfterAddingFile = (item) => {
      this.uploader.clearQueue();
      this.uploader.queue.push(item);
      this.userPhoto = this.preview(item);
      this.errorMessage = ""
    }
    this.uploader.onWhenAddingFileFailed = (item,filter,options) => this.onWhenAddingFileFailed(item,filter,options);
  }

  get f() { return this.editUserForm.controls; }

  onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
    switch (filter.name) {
      case 'fileSize':
        this.errorMessage = `Maximum upload size exceeded (${item.size} of 104800 allowed)`;
        break;
      case 'mimeType':
        this.errorMessage = `Type is not allowed. Allowed types: .jpeg, .png`;
        break;
        case 'queueLimit':
        this.errorMessage = `Maximum upload size (1 file) exceeded`;
        break;
      default:
        this.errorMessage = `Unknown error (filter is ${filter.name})`;
    }
  }
  getFormValidationErrors(form: FormGroup) {
    Object.keys(form.controls).forEach(key => {

      const controlErrors: ValidationErrors = form.get(key).errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          console.log('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
        });
      }
    });
  }

  GenderFullName(gender: string) {
    switch (gender) {
      case ('F'):
        return 'Female';
      case ('M'):
        return 'Male';
      case ('O'):
        return 'Other';
      default:
        return null;
    }
  }

  EditMode() {
    this.editMode = !this.editMode;
    if (this.editMode) {
      this.editUserForm.enable();
      this.f.birthDate.enable();
    }
    else {
      this.editUserForm.disable();
      this.f.birthDate.disable();
      this.uploader.clearQueue();
      this.userPhoto = this.currentUser.photo;
      this.gender = this.GenderFullName(this.currentUser.gender);
      this.submitted=false;
    }
    this.currentValues();
  }

  currentValues() {
    this.f.firstName.setValue(this.currentUser.firstName);
    this.f.lastName.setValue(this.currentUser.lastName);
    this.f.gender.setValue(this.GenderFullName(this.currentUser.gender));
    this.f.phone.setValue(this.currentUser.phone);
    if (this.currentUser.birthDate)
      this.f.birthDate.setValue(new Date(this.currentUser.birthDate));
    else
      this.f.birthDate.setValue('');
  }

  preview(fileItem): SafeUrl {

    fileItem.withCredentials = false;
    return this.sanitizer.bypassSecurityTrustUrl((window.URL.createObjectURL(fileItem._file)));
  }

  ChangePhoto() {
    let data = new FormData();
    let fileItem = this.uploader.queue[0]._file;
    console.log(fileItem.name);
    data.append('file', fileItem);
    this.userService.changePhoto(this.currentUser.id, data).subscribe(data => {
      console.log("Nova slika:" + data);
      this.currentUser.photo = data.toString();
      this.uploader.clearQueue();
    });
  }

  SaveChanges() {
    this.submitted = true;
    this.f.gender.setValue(this.gender);
    if (this.f.birthDate.value >= this.now) {
      this.f.birthDate.setErrors({ 'incorrect': true });
    }
    console.log(this.f.birthDate.value);
    if (this.editUserForm.invalid) {
      this.getFormValidationErrors(this.editUserForm);
      return;
    }
    else {
      this.currentUser.firstName = this.f.firstName.value;
      this.currentUser.lastName = this.f.lastName.value;
      this.currentUser.gender = this.f.gender.value.toString()[0];
      this.currentUser.birthDate = this.f.birthDate.value;
      this.currentUser.phone = this.f.phone.value;

      console.log(JSON.stringify(this.currentUser));

      this.editUserForm.disable();

      if (this.uploader.queue.length) {
        let data = new FormData();
        let fileItem = this.uploader.queue[0]._file;
        console.log(fileItem.name);
        data.append('file', fileItem);
        this.userService.changePhoto(this.currentUser.id, data).subscribe(data => {
          console.log("Nova slika:" + data);
          this.currentUser.photo = data.toString();
          this.uploader.clearQueue();
          this.userService.updateUser(this.currentUser).subscribe(data => {
            if (data != null) {
              this.currentUser = data;
              this.EditMode();
              this.msgSaved = true;
            }
            else {
              alert("Greska pri cuvanju promjena");
            }
          });
        });
      }
      else {
        this.userService.updateUser(this.currentUser).subscribe(data => {
          if (data != null) {
            this.currentUser = data;
            this.EditMode();
            this.msgSaved = true;
          }
          else {
            alert("Greska pri cuvanju promjena");
          }
        });
      }
    }
  }

  
  
}




