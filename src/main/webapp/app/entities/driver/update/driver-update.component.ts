import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { IDriver } from "../driver.model";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { DriverService } from "../driver.service";
import { Location } from "@angular/common";
import { CategoryService } from "../../category/category.service";
import { ICategory } from "../../category/category.model";

@Component({
    selector: 'app-driver-update',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './driver-update.component.html',
    styleUrls: ['./driver-update.component.scss'],
})
export class DriverUpdateComponent {

    driverId: number = 0;
    editDriver!: IDriver;
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
        private route: ActivatedRoute,
        private driverService: DriverService,
        private categoryService: CategoryService,
        private router: Router,
        private location: Location,
    ) {
        this.categoryService.getAll().subscribe(res => {
            this.categoryList = res;
        });
        this.driverId = this.route.snapshot.params['id'];
        this.getDriverById();
    }

    getDriverById() {
        this.driverService.find(this.driverId).subscribe({
            next: (response) => {
                if (response) {
                    this.editDriver = response;
                    const matchedCategory = this.categoryList.find(
                        cat => cat.id === this.editDriver?.category?.id
                    );
                    if(this.editDriver) {
                        this.driverForm.patchValue({
                            name: this.editDriver.name,
                            dob: this.editDriver.dob,
                            active: this.editDriver.active,
                            category: matchedCategory ?? null  
                        });
                    }
                } else {
                    this.error = true;
                }
            },
            error: (err => console.log('Error ', err))
        });
    }

    onSubmit() {
        const { name, dob, active, category } = this.driverForm.getRawValue();
        const driverUpdate: IDriver = {
            id: this.driverId,
            name: name,
            dob: dob,
            active: active,
            category: category!
        }
        this.driverService.update(this.driverId, driverUpdate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error update ', err)},
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