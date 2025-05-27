import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../../core/config/application-config.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { IDriver } from "./driver.model";


@Injectable({ providedIn: 'root' })
export class DriverService {
    protected resourceUrl: string;

    constructor(
        private applicationConfigService: ApplicationConfigService,
        private http: HttpClient
    ) {
        this.resourceUrl = this.applicationConfigService.getEndpointPrefix('api/drivers');
    }

    getAll(): Observable<IDriver[]> {
        return this.http.get<IDriver[]>(this.resourceUrl, { observe: 'body' });
    }

    find(id: number): Observable<IDriver> {
        return this.http.get<IDriver>(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }

    create(driver: IDriver): Observable<IDriver> {
        return this.http.post<IDriver>(this.resourceUrl, driver, { observe: 'body' });
    }

    update(id: number, category: IDriver): Observable<IDriver> {
        return this.http.put<IDriver>(`${this.resourceUrl}/${id}`, category, { observe: 'body'});
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }
}