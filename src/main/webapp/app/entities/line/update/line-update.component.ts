import { Component } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ILine } from "../line.model";
import { ActivatedRoute, Router } from "@angular/router";
import { LineService } from "../line.service";
import { Location } from "@angular/common";

@Component({
    selector: 'app-line-update',
    standalone: true,
    imports: [
        SharedModule,
        ReactiveFormsModule,
    ],
    templateUrl: './line-update.component.html',
    styleUrls: ['./line-update.component.scss'],
})
export class LineUpdateComponent {

    lineId: number = 0;
    editLine!: ILine;
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
        private route: ActivatedRoute,
        private lineService: LineService,
        private router: Router,
        private location: Location,
    ) {
        this.lineId = this.route.snapshot.params['id'];
        this.getLineById();
    }

    getLineById() {
        this.lineService.find(this.lineId).subscribe({
            next: (response) => {
                if (response) {
                    this.editLine = response;
                    if(this.editLine) {
                        this.lineForm.patchValue({
                            name: this.editLine.name,
                            start_time: this.editLine.start_time,
                            end_time: this.editLine.end_time,
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
        const { name, start_time, end_time } = this.lineForm.getRawValue();
        const lineUpdate: ILine = {
            id: this.lineId,
            name: name,
            start_time: start_time,
            end_time: end_time
        }
        this.lineService.update(this.lineId, lineUpdate).subscribe({
            next: () => { this.success = true },
            error: (err) => { this.error = true, console.log('Error update ', err)},
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