import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { LineService } from "../line.service";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { ILine } from "../line.model";

@Component({
    selector: 'app-line-new',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './line-new.component.html',
    styleUrls: ['./line-new.component.scss'],
})
export class LineNewComponent {

    success = false;
    error = false;

    lineForm = new FormGroup({
        name: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        }), 
        start_time: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        }), 
        end_time: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, ],
        }), 
    })

    constructor(
        private lineService: LineService,
        private router: Router,
        private location: Location,
    ) {}

    onSubmit() {
        const { name, start_time, end_time } = this.lineForm.getRawValue();
        const lineCreate: ILine = {
            name: name,
            start_time: start_time,
            end_time: end_time
        }
        this.lineService.create(lineCreate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error create ', err)},
            complete: () => this.setDelayAndTransferToAnotherPage(3000)
        });
    }

    setDelayAndTransferToAnotherPage(time: number) {
        if(time>0) {
            setTimeout(() => {
                this.router.navigate(['/lines']);
            }, time);
        } 
    }

    cancel() {
        this.location.back();
    }
    
}