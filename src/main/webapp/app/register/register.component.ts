import { Component } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from "../services/auth.service";
import { Router } from "@angular/router";
import { RegisterDto } from "../core/auth/register-dto.model";
import SharedModule from "../shared/shared.module";
import { HttpErrorResponse } from "@angular/common/http";
import { EMAIL_ALREADY_USED_TYPE } from "../core/config/error.constants";

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {

  doNotMatch = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;
  error = false;

  registerForm = new FormGroup({
    fullName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(2), Validators.maxLength(250)]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    })
  })

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.doNotMatch = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const { password, confirmPassword, fullName } = this.registerForm.getRawValue();
    
    if (password != confirmPassword) {
      this.doNotMatch = true;
    } else {
      const { email } = this.registerForm.getRawValue();
      const registerDto: RegisterDto = {
        fullName: fullName,
        email: email,
        password: password
      };
      this.authService.register(registerDto).subscribe({
        next: () => (this.success = true),
        error: response => this.processError(response),
        complete: () => this.setDelayAndTransferToAnotherPage(5000)
      });
    }
  }

  processError( response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  setDelayAndTransferToAnotherPage(time: number) {
    if(time>0) {
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, time);
    } 
  }
}