import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { CategoryListComponent } from "./list/category-list.component";
import { CategoryUpdateComponent } from "./update/category-update.component";
import { AuthGuard } from "../../core/auth/auth.guard";
import { CategoryNewComponent } from "./new/category-new.component";

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: '',
                component: CategoryListComponent,
                data: { pageTitle: 'Categories' },
                canActivate: [AuthGuard]
            },
            {
                path: 'new',
                component: CategoryNewComponent,
                data: { pageTitle: 'Category' },
                canActivate: [AuthGuard]
            },
            {
                path: 'edit/:id',
                component: CategoryUpdateComponent,
                data: { pageTitle: 'Category' },
                canActivate: [AuthGuard]
            },
        ])
    ]
})
export class CategoryRoutingModule{}