import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { ActivatedRoute, Router } from "@angular/router";
import { ICategory } from "../category.model";
import { CategoryService } from "../category.service";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Location } from "@angular/common";

@Component({
    selector: 'app-category-update',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './category-update.component.html',
    styleUrls: ['./category-update.component.scss'],
})
export class CategoryUpdateComponent {

    categoryId: number = 0;
    editCategory!: ICategory;
    success = false;
    error = false;

    categoryForm = new FormGroup({
        name: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        })
    })

    constructor(
        private route: ActivatedRoute,
        private categoryService: CategoryService,
        private router: Router,
        private location: Location,
    ) {
        this.categoryId = this.route.snapshot.params['id'];
        this.getCategoryById();
    }

    getCategoryById() {
        this.categoryService.find(this.categoryId).subscribe({
            next: (response) => {
                if (response) {
                    this.editCategory = response;
                    if(this.editCategory) {
                        this.categoryForm.controls['name'].setValue( this.editCategory.name);
                    }
                } else {
                    this.error = true;
                }
            },
            error: (err => console.log('Error ', err))
        });
    }

    onSubmit() {
        const { name } = this.categoryForm.getRawValue();
        const categoryUpdate: ICategory = {
            id: this.categoryId,
            name: name
        }
        this.categoryService.update(this.categoryId, categoryUpdate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error update ', err)},
            complete: () => this.setDelayAndTransferToAnotherPage(3000)
        });
    }

    setDelayAndTransferToAnotherPage(time: number) {
        if(time>0) {
            setTimeout(() => {
                this.router.navigate(['/categories']);
            }, time);
        } 
    }

    cancel() {
        this.location.back();
    }

}