import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './entities/home/home.component';
import { AuthGuard } from './core/auth/auth.guard';
import { NavbarComponent } from './layouts/navbar/navbar.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        title: 'Home',
        canActivate: [AuthGuard],
    },
    {
        path: '',
        component: NavbarComponent,
        outlet: 'navbar',
    },
    {
        path: 'login', 
        component: LoginComponent,
        title: 'Login Page',
        data: { hideNavbar: true }
    }, 
    {
        path: 'register', 
        component: RegisterComponent,
        title: 'Register Page',
        data: { hideNavbar: true }
    },
    {
        path: 'categories',
        canActivate: [AuthGuard],
        loadChildren: () => import('./entities/category/categoryRouting.module').then(m => m.CategoryRoutingModule),
    },
    {
        path: 'drivers',
        canActivate: [AuthGuard],
        loadChildren: () => import('./entities/driver/driverRouting.module').then(m => m.DriverRoutingModule),
    },
    {
        path: 'lines',
        canActivate: [AuthGuard],
        loadChildren: () => import('./entities/line/lineRouting.module').then(m => m.LineRoutingModule),
    },
    {
        path: 'shifts',
        canActivate: [AuthGuard],
        loadChildren: () => import('./entities/shift/shiftRouting.module').then(m => m.ShiftRoutingModule),
    },
    {
        path: '**',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];
