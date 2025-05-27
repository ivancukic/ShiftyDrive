import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ILine } from "../../line/line.model";
import { Router } from "@angular/router";
import { ShiftService } from "../shift.service";
import { IDriver } from "../../driver/driver.model";
import { IShiftCreate } from "../shift-create.model";
import { Location } from "@angular/common";
import { DriverService } from "../../driver/driver.service";
import { ShiftLineService } from "../../../services/shift-line.service";

@Component({
    selector: 'app-shift-new',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './shift-new.component.html',
    styleUrls: ['./shift-new.component.scss'],
})
export class ShiftNewComponent implements OnInit, OnDestroy {

    line: ILine | null = null;
    drivers: IDriver[] = [];
    success = false;
    error = false;
    errSameDriver = false;
    errSameShiftDiffLine = false;
    errMessage: string = '';

    shiftForm = new FormGroup({
        lineForm: new FormControl<ILine | null>(null),
        driversForm: new FormArray<FormControl<IDriver | null>>([]),
    })

    constructor(
        private shiftLineService: ShiftLineService,
        private shiftService: ShiftService,
        private router: Router,
        private location: Location,
        private driverService: DriverService,
    ) {}

    ngOnInit(): void {
        this.line = this.shiftLineService.getLine();
        if(!this.line) {
            console.log('No line found, redirecting...');
            this.setDelayAndTransferToAnotherPage(3000, 'lines');
        }
        this.driverService.getAll().subscribe(res => {
            this.drivers = res;
        });
        this.initializeDriverFormControls();
    }

    initializeDriverFormControls(): void {
        const formArray = this.shiftForm.get('driversForm') as FormArray<FormControl<IDriver | null>>;
        formArray.clear();

        const numDrivers = this.line?.num_of_drivers ?? 0;

        for (let i = 0; i < numDrivers; i++) {
            formArray.push(new FormControl<IDriver | null>(null));
        }
    }

    onSubmit() {
        const driversArray = this.shiftForm.get('driversForm') as FormArray<FormControl<IDriver | null>>;
        const validDrivers: IDriver[] = driversArray.controls
            .map(ctrl => ctrl.value)
            .filter((d): d is IDriver => d !== null);
            
        if (!validDrivers.length) {
            this.error = true;
            console.warn('No valid drivers selected.');
            return;
        }

        const shiftCreate: IShiftCreate = {
            line: this.line,
            drivers: validDrivers
        }
        this.shiftService.assigneShift(shiftCreate).subscribe({
            next: () => { this.success = true },
            error: (err) => { 
                // this.error = true, console.log('Error create ', err)
                if(err.error?.error==='SameDriverException') {
                    this.errSameDriver = true;
                    this.errMessage = err.error?.message;
                    console.log('Error create ', err);
                } else if (err.error?.error==='DriverOnSameShiftDiffLineException') {
                    this.errSameShiftDiffLine = true;
                    this.errMessage = err.error?.message;
                    console.log('Error create ', err);
                } else {
                    this.error = true;
                    console.log('Error create ', err);
                }
            },
            complete: () => this.setDelayAndTransferToAnotherPage(3000, 'shifts')
        });
    }

    setDelayAndTransferToAnotherPage(time: number, url: string) {
        if(time>0) {
            setTimeout(() => {
                this.router.navigate(['/', url]);
            }, time);
        } 
    }

    getDriverIndexes(): number[] {
        const formArray = this.shiftForm.get('driversForm') as FormArray;
        return Array.from({ length: formArray.length }, (_, i) => i);
    }

    cancel() {
        this.location.back();
    }

    ngOnDestroy(): void {
        this.shiftLineService.clear();
    }

}