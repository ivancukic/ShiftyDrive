import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { CategoryService } from "../category.service";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ICategory } from "../category.model";

@Component({
    selector: 'app-category-new',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './category-new.component.html',
    styleUrls: ['./category-new.component.scss'],
})
export class CategoryNewComponent {

    success = false;
    error = false;

    categoryForm = new FormGroup({
        name: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        })
    })

    constructor(
        private categoryService: CategoryService,
        private router: Router,
        private location: Location,
    ) {}

    onSubmit() {
        const { name } = this.categoryForm.getRawValue();
        const categoryCreate: ICategory = {
            name: name
        }
        this.categoryService.create(categoryCreate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error create ', err)},
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