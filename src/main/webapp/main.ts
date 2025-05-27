import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { ApplicationConfigService } from './app/core/config/application-config.service';
import { environment } from './environments/environment';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/core/interceptor/auth.interceptor';

const appService = new ApplicationConfigService();
appService.setEndpointPrefix(environment.apiUrl);

const appConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptor]) 
    ),
    { provide: ApplicationConfigService, useValue: appService }
  ]
};

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
