import { Component, OnInit } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { CategoryService } from "../category.service";
import { ICategory } from "../category.model";
import { Location } from "@angular/common";
import { FormsModule } from "@angular/forms";

@Component({
    selector: 'app-category-list',
    standalone: true,
    imports: [
        SharedModule,
        FormsModule,
    ],
    templateUrl: './category-list.component.html',
    styleUrls: ['./category-list.component.scss'],
})
export class CategoryListComponent implements OnInit {

    categoryList: ICategory[] = [];
    nameFilter: string = '';

    constructor(
        private categoryService: CategoryService,
        private location: Location,
    ) {}

    ngOnInit(): void {
        this.categoryService.getAll().subscribe( res => {
            this.categoryList = res;
        });
    }

    delete(category: ICategory): void {
        if(confirm('Are you sure you want to delete?')) {
            this.categoryService.delete(category.id!).subscribe({
            next: () => {window.location.reload()},
            error: (err) => {console.log('Error delete ', err)}
        });
        }
    }

    inputFilter(filter: any) {
        const name = filter.value;
        this.categoryService.filterCategories(name).subscribe({
            next: (res) => { this.categoryList = res },
            error: (err) => { console.log('Filter err ' + err) }
        })
    }

}