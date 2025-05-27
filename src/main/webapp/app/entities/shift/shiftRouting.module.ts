import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { ShiftListComponent } from "./list/shift-list.component";
import { AuthGuard } from "../../core/auth/auth.guard";
import { ShiftNewComponent } from "./new/shift-new.component";
import { ShiftUpdateComponent } from "./update/shift-update.component";

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: '',
                component: ShiftListComponent,
                data: { pageTitle: 'Shifts' },
                canActivate: [AuthGuard]
            },
            {
                path: 'new',
                component: ShiftNewComponent,
                data: { pageTitle: 'Shift' },
                canActivate: [AuthGuard]
            },
            {
                path: 'edit/:id',
                component: ShiftUpdateComponent,
                data: { pageTitle: 'Shift' },
                canActivate: [AuthGuard]
            },
        ])
    ]
})
export class ShiftRoutingModule{}