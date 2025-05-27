import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "../core/config/application-config.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { IUploadFileLine } from "../model/upload-file-line.model";
import { IUploadFileDriver } from "../model/upload-file-driver.model";

@Injectable({ providedIn: 'root' })
export class UploadFileService {
    protected resourceUrl: string;

    constructor(
        private applicationConfigService: ApplicationConfigService,
        private http: HttpClient,
    ) {
        this.resourceUrl = this.applicationConfigService.getEndpointPrefix('api/upload');
    }

    uploadLineFile(file: any): Observable<IUploadFileLine> {
        return this.http.post<IUploadFileLine>(this.resourceUrl + '/line-stage', file, { observe: 'body' });
    }

    uploadDriverFile(file: any): Observable<IUploadFileDriver> {
        return this.http.post<IUploadFileDriver>(this.resourceUrl + '/driver-stage', file, { observe: 'body' });
    }
}