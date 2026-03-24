# Keycloak OAuth 2.0 Setup Guide

This guide explains how to configure Keycloak for OAuth 2.0 authentication with this Angular application.

## Prerequisites

- Keycloak server running (locally or remotely)
- Admin access to Keycloak

## Keycloak Configuration Steps

### 1. Create a Realm

1. Log in to Keycloak Admin Console (usually at `http://localhost:8080`)
2. Click on the realm dropdown (top-left corner)
3. Click "Create Realm"
4. Enter a realm name (e.g., `ada-course-realm`)
5. Click "Create"

### 2. Create a Client

1. In your realm, go to "Clients" in the left menu
2. Click "Create client"
3. Configure the client:
   - **Client ID**: `angular-client` (must match `clientId` in `auth.config.ts`)
   - **Client Protocol**: `openid-connect`
   - Click "Next"

4. Configure capability:
   - **Client authentication**: OFF (public client)
   - **Authorization**: OFF
   - **Authentication flow**: 
     - ✅ Standard flow
     - ✅ Direct access grants
   - Click "Next"

5. Configure login settings:
   - **Root URL**: `http://localhost:4200`
   - **Home URL**: `http://localhost:4200`
   - **Valid redirect URIs**: 
     - `http://localhost:4200/*`
   - **Valid post logout redirect URIs**: 
     - `http://localhost:4200/*`
   - **Web origins**: 
     - `http://localhost:4200`
     - `+` (to allow CORS from valid redirect URIs)
   - Click "Save"

### 3. Create Test Users

1. Go to "Users" in the left menu
2. Click "Add user"
3. Fill in user details:
   - **Username**: `testuser`
   - **Email**: `testuser@example.com`
   - **First name**: `Test`
   - **Last name**: `User`
   - **Email verified**: ON
4. Click "Create"
5. Go to "Credentials" tab
6. Click "Set password"
7. Enter a password and set "Temporary" to OFF
8. Click "Save"

### 4. Configure Client Scopes (Optional)

The default scopes (openid, profile, email) are usually sufficient. If you need custom scopes:

1. Go to "Client scopes" in the left menu
2. Create custom scopes as needed
3. Assign them to your client in the "Client scopes" tab of your client

## Application Configuration

### Update `src/app/config/auth.config.ts`

Update the following values in your `auth.config.ts` file:

```typescript
export const authConfig: AuthConfig = {
  // Replace with your Keycloak server URL and realm name
  issuer: 'http://localhost:8080/realms/ada-course-realm',
  
  // Must match the Client ID created in Keycloak
  clientId: 'angular-client',
  
  // Your Angular app URL
  redirectUri: window.location.origin,
  
  // Scopes to request
  scope: 'openid profile email',
  
  // ... other settings
};
```

### Environment-Specific Configuration

For different environments (dev, staging, production), you can create environment files:

**src/environments/environment.ts** (Development):
```typescript
export const environment = {
  production: false,
  keycloak: {
    issuer: 'http://localhost:8080/realms/ada-course-realm',
    clientId: 'angular-client',
    redirectUri: 'http://localhost:4200'
  }
};
```

**src/environments/environment.prod.ts** (Production):
```typescript
export const environment = {
  production: true,
  keycloak: {
    issuer: 'https://keycloak.yourdomain.com/realms/ada-course-realm',
    clientId: 'angular-client',
    redirectUri: 'https://yourdomain.com'
  }
};
```

Then update `auth.config.ts` to use these values:
```typescript
import { environment } from '../../environments/environment';

export const authConfig: AuthConfig = {
  issuer: environment.keycloak.issuer,
  clientId: environment.keycloak.clientId,
  redirectUri: environment.keycloak.redirectUri,
  // ... other settings
};
```

## Testing the Integration

### 1. Start Keycloak

```bash
# If using Docker
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```

### 2. Start the Angular Application

```bash
npm start
```

### 3. Test the Login Flow

1. Navigate to `http://localhost:4200`
2. You should be redirected to the login page
3. Click "Sign in with Keycloak"
4. You'll be redirected to Keycloak login page
5. Enter the test user credentials
6. After successful login, you'll be redirected back to the application
7. You should see your username in the header with a logout button

### 4. Test Protected Routes

- Try accessing `/courses`, `/transfer`, or `/users/new`
- These routes are protected and require authentication
- If not authenticated, you'll be redirected to login

### 5. Test Logout

- Click the "Logout" button in the header
- You should be logged out and redirected to the home page

## Troubleshooting

### CORS Issues

If you encounter CORS errors:
1. In Keycloak, go to your client settings
2. Add `http://localhost:4200` to "Web origins"
3. Or use `+` to allow all valid redirect URIs

### Invalid Redirect URI

If you get "Invalid redirect URI" error:
1. Check that your redirect URIs in Keycloak match your application URL
2. Make sure to include the wildcard: `http://localhost:4200/*`

### Token Expiration

Tokens expire after a certain time. The application uses silent refresh to automatically renew tokens. If you encounter issues:
1. Check that `silent-refresh.html` exists in the `public` folder
2. Verify the `silentRefreshRedirectUri` in `auth.config.ts`

### Debug Mode

Enable debug mode in `auth.config.ts`:
```typescript
showDebugInformation: true
```

This will log OAuth events to the browser console.

## Security Best Practices

### Production Deployment

1. **Use HTTPS**: Always use HTTPS in production
   ```typescript
   requireHttps: true  // Set to true in production
   ```

2. **Secure Client**: Consider using a confidential client with client secret for backend applications

3. **Token Storage**: Tokens are stored in sessionStorage by default. For better security, consider:
   - Using httpOnly cookies (requires backend support)
   - Implementing token encryption

4. **PKCE**: The application uses PKCE (Proof Key for Code Exchange) by default, which is recommended for public clients

5. **Token Validation**: The library validates tokens automatically, but ensure:
   - Token signature verification is enabled
   - Token expiration is checked
   - Issuer validation is performed

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [angular-oauth2-oidc Documentation](https://github.com/manfredsteyer/angular-oauth2-oidc)
- [OAuth 2.0 Authorization Code Flow with PKCE](https://oauth.net/2/pkce/)
- [OpenID Connect](https://openid.net/connect/)

## Support

For issues or questions:
1. Check the browser console for error messages
2. Enable debug mode in `auth.config.ts`
3. Review Keycloak server logs
4. Verify all configuration values match between Keycloak and the application