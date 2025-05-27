import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../../core/config/application-config.service";
import { HttpClient } from "@angular/common/http";
import { ILine } from "./line.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class LineService {
    protected resourceUrl: string;

    constructor(
        private applicationConfigService: ApplicationConfigService,
        private http: HttpClient,
    ) {
        this.resourceUrl = this.applicationConfigService.getEndpointPrefix('api/lines');
    }

    getAll(): Observable<ILine[]> {
        return this.http.get<ILine[]>(this.resourceUrl, { observe: 'body' });
    }

    find(id: number): Observable<ILine> {
        return this.http.get<ILine>(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }

    create(line: ILine): Observable<ILine> {
        return this.http.post<ILine>(this.resourceUrl, line, { observe: 'body' });
    }

    update(id: number, line: ILine): Observable<ILine> {
        return this.http.put<ILine>(`${this.resourceUrl}/${id}`, line, { observe: 'body'});
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }
}