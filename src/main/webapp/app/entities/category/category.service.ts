import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../../core/config/application-config.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ICategory } from "./category.model";

@Injectable({ providedIn: 'root' })
export class CategoryService {
    protected resourceUrl: string;

    constructor(
        private applicationConfigService: ApplicationConfigService,
        private http: HttpClient
    ) {
        this.resourceUrl = this.applicationConfigService.getEndpointPrefix('api/categories');
    }

    getAll(): Observable<ICategory[]> {
        return this.http.get<ICategory[]>(this.resourceUrl, { observe: 'body' });
    }

    find(id: number): Observable<ICategory> {
        return this.http.get<ICategory>(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }

    create(category: ICategory): Observable<ICategory> {
        return this.http.post<ICategory>(this.resourceUrl, category, { observe: 'body' });
    }

    update(id: number, category: ICategory): Observable<ICategory> {
        return this.http.put<ICategory>(`${this.resourceUrl}/${id}`, category, { observe: 'body'});
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'body' });
    }
}