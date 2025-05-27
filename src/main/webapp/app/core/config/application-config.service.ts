import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class ApplicationConfigService {
  private endpointPrefix: string = '';

  setEndpointPrefix(prefix: string): void {
    this.endpointPrefix = prefix;
  }

  getEndpointPrefix(path: string): string {
    return `${this.endpointPrefix}/${path}`;
  }
}