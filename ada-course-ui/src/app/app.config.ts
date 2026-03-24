import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideKeycloak } from 'keycloak-angular';

import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';
import { keycloakConfig } from './config/auth.config';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideKeycloak({
      config: keycloakConfig,
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: false,
        pkceMethod: 'S256'
      }
    }),
    provideRouter(routes),
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor])
    )
  ]
};

// Made with Bob
