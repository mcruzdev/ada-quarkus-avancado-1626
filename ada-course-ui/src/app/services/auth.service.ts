import { Injectable, inject, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly keycloak = inject(Keycloak);
  private readonly router = inject(Router);

  private readonly userProfileSignal = signal<Keycloak.KeycloakProfile | null>(null);
  private profileLoaded = false;

  readonly isAuthenticated = computed(() => this.keycloak.authenticated ?? false);
  readonly userProfile = this.userProfileSignal.asReadonly();
  readonly userName = computed(() => {
    const profile = this.userProfileSignal();
    if (!profile && this.keycloak.authenticated && !this.profileLoaded) {
      // Lazy load profile when accessed
      this.loadUserProfile();
    }
    return profile?.firstName || profile?.username || this.keycloak.tokenParsed?.['preferred_username'] || 'User';
  });

  async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: window.location.origin
    });
  }

  async logout(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: window.location.origin
    });
  }

  private async loadUserProfile(): Promise<void> {
    if (this.keycloak.authenticated && !this.profileLoaded) {
      this.profileLoaded = true;
      try {
        const profile = await this.keycloak.loadUserProfile();
        this.userProfileSignal.set(profile);
      } catch (error) {
        console.error('Error loading user profile:', error);
        // Fallback to token data
        if (this.keycloak.tokenParsed) {
          this.userProfileSignal.set({
            username: this.keycloak.tokenParsed['preferred_username'],
            email: this.keycloak.tokenParsed['email'],
            firstName: this.keycloak.tokenParsed['given_name'],
            lastName: this.keycloak.tokenParsed['family_name']
          });
        }
      }
    }
  }

  async getToken(): Promise<string> {
    try {
      await this.keycloak.updateToken(5);
      return this.keycloak.token || '';
    } catch (error) {
      console.error('Failed to refresh token:', error);
      return '';
    }
  }

  isLoggedIn(): boolean {
    return this.keycloak.authenticated ?? false;
  }

  hasValidToken(): boolean {
    return this.keycloak.authenticated ?? false;
  }

  getUserRoles(): string[] {
    return this.keycloak.realmAccess?.roles || [];
  }

  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }
}

// Made with Bob
