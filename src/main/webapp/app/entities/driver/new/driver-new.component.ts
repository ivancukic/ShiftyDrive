import { Component, OnInit } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CategoryService } from "../../category/category.service";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { DriverService } from "../driver.service";
import { IDriver } from "../driver.model";
import { ICategory } from "../../category/category.model";

@Component({
    selector: 'app-driver-new',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './driver-new.component.html',
    styleUrls: ['./driver-new.component.scss'],
})
export class DriverNewComponent implements OnInit {

    success = false;
    error = false;
    categoryList: ICategory[] = [];

    driverForm = new FormGroup({
        name: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        }),
        dob: new FormControl(new Date(), {
            nonNullable: true,
            validators: [Validators.required, ],
        }),
        active: new FormControl(true, {
            nonNullable: true,
        }),
        category: new FormControl<ICategory | null>(null),
    })

    constructor(
        private driverService: DriverService,
        private categoryService: CategoryService,
        private router: Router,
        private location: Location,
    ) {}

    ngOnInit(): void {
        this.categoryService.getAll().subscribe(res => {
            this.categoryList = res;
        });
    }

    onSubmit() {
        const { name, dob, active, category } = this.driverForm.getRawValue();
        const driverCreate: IDriver = {
            name: name,
            dob: dob,
            active: active,
            category: category ?? undefined
        }
        this.driverService.create(driverCreate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error create ', err)},
            complete: () => this.setDelayAndTransferToAnotherPage(3000)
        });
    }

    setDelayAndTransferToAnotherPage(time: number) {
        if(time>0) {
            setTimeout(() => {
                this.router.navigate(['/drivers']);
            }, time);
        } 
    }

    cancel() {
        this.location.back();
    }
    
}