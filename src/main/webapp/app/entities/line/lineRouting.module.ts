import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { LineListComponent } from "./list/line-list.component";
import { AuthGuard } from "../../core/auth/auth.guard";
import { LineNewComponent } from "./new/line-new.component";
import { LineUpdateComponent } from "./update/line-update.component";

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: '',
                component: LineListComponent,
                data: { pageTitle: 'Lines' },
                canActivate: [AuthGuard]
            },
            {
                path: 'new',
                component: LineNewComponent,
                data: { pageTitle: 'Line' },
                canActivate: [AuthGuard]
            },
            {
                path: 'edit/:id',
                component: LineUpdateComponent,
                data: { pageTitle: 'Line' },
                canActivate: [AuthGuard]
            },
        ])
    ]
})
export class LineRoutingModule{}