import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import Keycloak from 'keycloak-js';

export const authGuard: CanActivateFn = (route, state) => {
  const keycloak = inject(Keycloak);
  const router = inject(Router);

  const isLoggedIn = keycloak.authenticated ?? false;

  if (!isLoggedIn) {
    // Store the attempted URL for redirecting after login
    sessionStorage.setItem('returnUrl', state.url);
    
    // Redirect to login page
    router.navigate(['/login']);
    return false;
  }

  return true;
};

// Made with Bob
