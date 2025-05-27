import { Component, OnInit } from "@angular/core";
import SharedModule from "../../../shared/shared.module";
import { IShiftCreate } from "../shift-create.model";
import { ShiftService } from "../shift.service";
import { Location } from "@angular/common";
import { IShift } from "../shift.model";

@Component({
    selector: 'app-shift-list',
    standalone: true,
    imports: [
        SharedModule,
    ],
    templateUrl: './shift-list.component.html',
    styleUrls: ['./shift-list.component.scss'],
})
export class ShiftListComponent implements OnInit {

    shiftList: IShift[] = [];

    constructor(
        private shiftService: ShiftService,
        private location: Location,
    ) {}

    ngOnInit(): void {
        this.shiftService.getAll().subscribe( res => {
            this.shiftList = res;
        });
    }

    delete(shift: IShift): void {
        if(confirm('Are you sure you want to delete?')) {
            this.shiftService.delete(shift.id!).subscribe({
            next: () => {window.location.reload()},
            error: (err) => {console.log('Error delete ', err)}
        });
        }
    }

}