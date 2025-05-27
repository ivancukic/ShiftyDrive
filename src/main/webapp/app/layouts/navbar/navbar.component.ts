import { Component } from "@angular/core";
import { Router, RouterModule } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [RouterModule],
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {

    constructor(
        private authService: AuthService,
        private router: Router,
    ) {}

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }

}