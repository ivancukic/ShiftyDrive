import { Component } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import SharedModule from "../shared/shared.module";
import { AuthService } from "../services/auth.service";
import { Router } from "@angular/router";
import { LoginDto } from "../core/auth/login-dto.model";
import { HttpErrorResponse } from "@angular/common/http";
import { INVALID_CREDENTIALS } from "../core/config/error.constants";

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent {

  success = false;
  error = false;

  email = '';
  password = '';
  errorMessage = '';

  loginForm = new FormGroup({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    })
  })

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    const { email, password } = this.loginForm.getRawValue();
    const loginDto: LoginDto = {
      email: email,
      password: password
    };
    this.authService.login(loginDto).subscribe({
      next: () => (this.success = true),
      error: (response => this.processError(response)),
      complete: () => this.setDelayAndTransferToAnotherPage(3000)
    });
  }  

  processError( response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === INVALID_CREDENTIALS) {
      this.error = true;
    } else {
      this.error = true;
    }
  }

  setDelayAndTransferToAnotherPage(time: number) {
    if(time>0) {
      setTimeout(() => {
        this.router.navigate(['']);
      }, time);
    } 
  }
}