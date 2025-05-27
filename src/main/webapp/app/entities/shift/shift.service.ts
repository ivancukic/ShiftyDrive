import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../../core/config/application-config.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { IShiftCreate } from "./shift-create.model";
import { IShift } from "./shift.model";
import { IShiftChange } from "./shift-change.model";

@Injectable({ providedIn: 'root' })
export class ShiftService {
    protected resourceUrl: string;

    constructor(
        private applicationConfigService: ApplicationConfigService,
        private http: HttpClient
    ) {
        this.resourceUrl = this.applicationConfigService.getEndpointPrefix('api/drivers-shifts');
    }

    getAll(): Observable<IShift[]> {
        return this.http.get<IShift[]>(this.resourceUrl, { observe: 'body' });
    }

    find(id: number): Observable<IShift> {
        return this.http.get<IShift>(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }

    create(shift: IShift): Observable<IShift> {
        return this.http.post<IShift>(this.resourceUrl, shift, { observe: 'body' });
    }

    update(id: number, shift: IShift): Observable<IShift> {
        return this.http.put<IShift>(`${this.resourceUrl}/${id}`, shift, { observe: 'body'});
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }

    assigneShift(shifts: IShiftCreate): Observable<IShiftCreate> {
        return this.http.post<IShiftCreate>(this.resourceUrl + '/new-shift', shifts, { observe: 'body' });
    }

    changeShift(id: number, shift: IShiftChange): Observable<IShiftChange> {
        return this.http.put<IShiftChange>(`${this.resourceUrl}/change-shift/${id}`, shift, { observe: 'body'});
    }
}