import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.authService.isLoggedIn().pipe(
      map((loggedIn) => {
        if (!loggedIn) {
          this.router.navigate(['/login'], {
            queryParams: { returnUrl: state.url }, 
          });
          return false;
        }
        return true;
      }),
      catchError(() => {
        this.router.navigate(['/login'], {  
          queryParams: { returnUrl: state.url },
        });
        return of(false); 
      })
    );
  }
}