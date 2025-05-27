import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { DriverListComponent } from "./list/driver-list.component";
import { AuthGuard } from "../../core/auth/auth.guard";
import { DriverNewComponent } from "./new/driver-new.component";
import { DriverUpdateComponent } from "./update/driver-update.component";

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: '',
                component: DriverListComponent,
                data: { pageTitle: 'Drivers' },
                canActivate: [AuthGuard]
            },
            {
                path: 'new',
                component: DriverNewComponent,
                data: { pageTitle: 'Driver' },
                canActivate: [AuthGuard]
            },
            {
                path: 'edit/:id',
                component: DriverUpdateComponent,
                data: { pageTitle: 'Driver' },
                canActivate: [AuthGuard]
            },
        ])
    ]
})
export class DriverRoutingModule{}