import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { FormArray, FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { IShift } from "../shift.model";
import { IDriver } from "../../driver/driver.model";
import { ILine } from "../../line/line.model";
import { ActivatedRoute, Router } from "@angular/router";
import { DriverService } from "../../driver/driver.service";
import { ShiftService } from "../shift.service";
import { Location } from "@angular/common";
import { IShiftChange } from "../shift-change.model";

@Component({
    selector: 'app-shift-update',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './shift-update.component.html',
    styleUrls: ['./shift-update.component.scss'],
})
export class ShiftUpdateComponent {

    shiftId: number = 0;
    editShift!: IShift;
    success = false;
    error = false;
    driverList: IDriver[] = [];

    shiftForm = new FormGroup({
        lineForm: new FormControl<ILine | null>(null),
        driverForm: new FormControl<IDriver | null>(null),
    })

    constructor(
        private route: ActivatedRoute,
        private driverService: DriverService,
        private shiftService: ShiftService,
        private router: Router,
        private location: Location,
    ) {
        this.driverService.getAll().subscribe(res => {
            this.driverList = res;
        });
        this.shiftId = this.route.snapshot.params['id'];
        this.geShiftById();
    }

    geShiftById() {
        this.shiftService.find(this.shiftId).subscribe({
            next: (response) => {
                if (response) {
                    this.editShift = response;
                    const matchedDriver = this.driverList.find(
                        driver => driver.id === this.editShift?.driver?.id
                    );
                    if(this.editShift) {
                        this.shiftForm.patchValue({
                            lineForm: this.editShift.line,
                            driverForm: matchedDriver ?? null  
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
        const { driverForm } = this.shiftForm.getRawValue();
        const shiftUpdate: IShiftChange = {
            line: this.editShift.line,
            driver: driverForm,
            driversShifts: this.editShift
        }
        this.shiftService.changeShift(this.shiftId, shiftUpdate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error update ', err)},
            complete: () => this.setDelayAndTransferToAnotherPage(3000)
        });
    }

    setDelayAndTransferToAnotherPage(time: number) {
        if(time>0) {
            setTimeout(() => {
                this.router.navigate(['/shifts']);
            }, time);
        } 
    }

    cancel() {
        this.location.back();
    }

}