import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import Keycloak from 'keycloak-js';
import { from, switchMap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloak = inject(Keycloak);

  // Skip adding token for Keycloak endpoints
  if (req.url.includes('/realms/') || 
      req.url.includes('/protocol/openid-connect/')) {
    return next(req);
  }

  // Add Bearer token if user is logged in
  if (keycloak.authenticated) {
    return from(keycloak.updateToken(5)).pipe(
      switchMap(() => {
        const token = keycloak.token;
        if (token) {
          const clonedRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${token}`
            }
          });
          return next(clonedRequest);
        }
        return next(req);
      })
    );
  }

  return next(req);
};

// Made with Bob
