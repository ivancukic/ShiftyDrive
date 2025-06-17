import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../../core/config/application-config.service";
import { HttpClient, HttpParams } from "@angular/common/http";
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

    filterDrivers(name?: string, dobBefore?: string, dobAfter?: string, dobBetweenStart?: string, dobBetweenEnd?: string, active?: boolean, categoryId?: number): Observable<IDriver[]> {
        let params = new HttpParams();

        if(name) {
            params = params.set('name', name);
        }
        if(dobBefore) {
            params = params.set('dobBefore', dobBefore);
        }
        if (dobAfter) {
            params = params.set('dobAfter', dobAfter);
        }
        if(dobBetweenStart) {
            params = params.set('dobBetweenStart', dobBetweenStart);
        }
        if(dobBetweenEnd) {
            params = params.set('dobBetweenEnd', dobBetweenEnd);
        }
        if (active !== undefined && active !== null) {
            params = params.set('active', active);
        }
        if (categoryId !== undefined && categoryId !== null) {
            params = params.set('categoryId', categoryId);
        }
        return this.http.get<IDriver[]>(`${this.resourceUrl}/filter-drivers`, { params });
    }
}