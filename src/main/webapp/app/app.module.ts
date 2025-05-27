import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { BrowserModule } from "@angular/platform-browser";
import { ApplicationConfigService } from "./core/config/application-config.service";
import { environment } from "../environments/environment";
import { httpInterceptorProviders } from "./core/interceptor/http-interceptors.provider";

@NgModule({
    declarations: [AppComponent],
    imports: [
      BrowserModule,
      // other modules
    ],
    providers: [
      // httpInterceptorProviders,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
    constructor(applicationConfigService: ApplicationConfigService) {
      applicationConfigService.setEndpointPrefix(environment.apiUrl);
    }
}