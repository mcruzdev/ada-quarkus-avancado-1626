import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ProjetoFinal - Test suite for validating Quarkus Advanced Course final project requirements
 * 
 * This class tests all requirements from PROJETO_FINAL.md via REST API calls only.
 * Each validation counts as one point towards the final score.
 * 
 * Usage: java ProjetoFinal.java
 */
public class ProjetoFinal {
    
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static final List<String> results = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("PROJETO FINAL - QUARKUS AVANÇADO - TEST SUITE");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Run all tests via REST API
        testAdminUserLogin();
        testUserRegistrationEndpoint();
        testDuplicateEmail();
        testPasswordMinLength();
        testInvalidEmail();
        testRequiredName();
        testEmptyName();
        testAuthTokenEndpoint();
        testUsersMeEndpoint();
        testJwtAuthenticationOnWriteOperations();
        testRoleBasedAuthorizationAdmin();
        testRoleBasedAuthorizationUser();
        
        // Print results
        printResults();
    }
    
    // Test 1: Test if admin user exists and can login
    private static void testAdminUserLogin() {
        String testName = "Admin User Login (admin/admin)";
        totalTests++;
        
        try {
            String json = "{\"email\":\"admin\",\"password\":\"admin\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/token"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                String body = response.body();
                boolean hasToken = body.contains("token");
                
                if (hasToken) {
                    pass(testName, "Admin user exists and can authenticate");
                } else {
                    fail(testName, "Response missing 'token' field");
                }
            } else {
                fail(testName, "Admin login failed with status: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 2: Test POST /users endpoint
    private static void testUserRegistrationEndpoint() {
        String testName = "POST /users - User Registration Endpoint";
        totalTests++;
        
        try {
            String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
            String json = String.format(
                "{\"name\":\"Test User\",\"email\":\"%s\",\"password\":\"password123\"}", 
                uniqueEmail
            );
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                pass(testName, "User registration successful (status: " + response.statusCode() + ")");
            } else {
                fail(testName, "Unexpected status code: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Endpoint not accessible or error: " + e.getMessage());
        }
    }
    
    // Test 3: Duplicate email validation (409)
    private static void testDuplicateEmail() {
        String testName = "Duplicate Email Validation (409)";
        totalTests++;
        
        try {
            String duplicateEmail = "duplicate" + System.currentTimeMillis() + "@example.com";
            String json = String.format(
                "{\"name\":\"Test User\",\"email\":\"%s\",\"password\":\"password123\"}", 
                duplicateEmail
            );
            
            // First registration
            HttpRequest request1 = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            client.send(request1, HttpResponse.BodyHandlers.ofString());
            
            // Duplicate registration
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 409) {
                pass(testName, "Correctly returns 409 for duplicate email");
            } else {
                fail(testName, "Expected 409, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 4: Password minimum length validation (400)
    private static void testPasswordMinLength() {
        String testName = "Password Minimum Length Validation (400)";
        totalTests++;
        
        try {
            String json = "{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"short\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 400) {
                pass(testName, "Correctly returns 400 for short password");
            } else {
                fail(testName, "Expected 400, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 5: Invalid email format validation (400)
    private static void testInvalidEmail() {
        String testName = "Invalid Email Format Validation (400)";
        totalTests++;
        
        try {
            String json = "{\"name\":\"Test User\",\"email\":\"invalid-email\",\"password\":\"password123\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 400) {
                pass(testName, "Correctly returns 400 for invalid email");
            } else {
                fail(testName, "Expected 400, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 6: Required name validation (400)
    private static void testRequiredName() {
        String testName = "Required Name Validation (400)";
        totalTests++;
        
        try {
            String json = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 400) {
                pass(testName, "Correctly returns 400 for missing name");
            } else {
                fail(testName, "Expected 400, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 7: Empty/null name validation (400)
    private static void testEmptyName() {
        String testName = "Empty/Null Name Validation (400)";
        totalTests++;
        
        try {
            String json = "{\"name\":\"\",\"email\":\"test@example.com\",\"password\":\"password123\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 400) {
                pass(testName, "Correctly returns 400 for empty name");
            } else {
                fail(testName, "Expected 400, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 8: Test POST /auth/token endpoint with correct format
    private static void testAuthTokenEndpoint() {
        String testName = "POST /auth/token - JWT Token Format (token + expiresIn)";
        totalTests++;
        
        try {
            String json = "{\"email\":\"admin\",\"password\":\"admin\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/token"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                String body = response.body();
                boolean hasToken = body.contains("token");
                boolean hasExpiresIn = body.contains("expiresIn") || body.contains("expires_in");
                
                if (hasToken && hasExpiresIn) {
                    pass(testName, "Token endpoint returns correct JSON format");
                } else {
                    fail(testName, "Response missing 'token' or 'expiresIn' fields");
                }
            } else {
                fail(testName, "Expected 200, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 9: Test GET /users/me endpoint
    private static void testUsersMeEndpoint() {
        String testName = "GET /users/me - Authenticated User Info";
        totalTests++;
        
        try {
            // First, get a token
            String loginJson = "{\"email\":\"admin\",\"password\":\"admin\"}";
            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/token"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                    .build();
            
            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
            
            if (loginResponse.statusCode() == 200) {
                String token = extractToken(loginResponse.body());
                
                if (token != null) {
                    // Now test /users/me
                    HttpRequest meRequest = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/users/me"))
                            .header("Authorization", "Bearer " + token)
                            .GET()
                            .build();
                    
                    HttpResponse<String> meResponse = client.send(meRequest, HttpResponse.BodyHandlers.ofString());
                    
                    if (meResponse.statusCode() == 200) {
                        pass(testName, "Successfully retrieved user info with JWT");
                    } else {
                        fail(testName, "Expected 200, got: " + meResponse.statusCode());
                    }
                } else {
                    fail(testName, "Could not extract token from login response");
                }
            } else {
                fail(testName, "Could not authenticate to test endpoint");
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 10: Test JWT authentication on write operations
    private static void testJwtAuthenticationOnWriteOperations() {
        String testName = "JWT Authentication Required for Write Operations";
        totalTests++;
        
        try {
            // Try to create a course without authentication
            String json = "{\"name\":\"Test Course\",\"description\":\"Test\"}";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/courses"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 401 || response.statusCode() == 403) {
                pass(testName, "Write operation correctly requires authentication (status: " + response.statusCode() + ")");
            } else {
                fail(testName, "Expected 401/403, got: " + response.statusCode());
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 11: Test role-based authorization - ADMIN can write
    private static void testRoleBasedAuthorizationAdmin() {
        String testName = "Role-Based Authorization - ADMIN Can Write";
        totalTests++;
        
        try {
            // Get admin token
            String loginJson = "{\"email\":\"admin\",\"password\":\"admin\"}";
            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/token"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                    .build();
            
            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
            
            if (loginResponse.statusCode() == 200) {
                String token = extractToken(loginResponse.body());
                
                if (token != null) {
                    // Try to create a course with admin token
                    String json = "{\"name\":\"Admin Test Course\",\"description\":\"Test\"}";
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/courses"))
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + token)
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();
                    
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    
                    if (response.statusCode() == 200 || response.statusCode() == 201) {
                        pass(testName, "ADMIN role can perform write operations");
                    } else {
                        fail(testName, "ADMIN should be able to write, got: " + response.statusCode());
                    }
                } else {
                    fail(testName, "Could not extract admin token");
                }
            } else {
                fail(testName, "Could not authenticate as admin");
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Test 12: Test role-based authorization - USER cannot write
    private static void testRoleBasedAuthorizationUser() {
        String testName = "Role-Based Authorization - USER Cannot Write";
        totalTests++;
        
        try {
            // First create a regular user
            String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
            String registerJson = String.format(
                "{\"name\":\"Test User\",\"email\":\"%s\",\"password\":\"password123\"}", 
                uniqueEmail
            );
            
            HttpRequest registerRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(registerJson))
                    .build();
            
            HttpResponse<String> registerResponse = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());
            
            if (registerResponse.statusCode() == 200 || registerResponse.statusCode() == 201) {
                // Now login as that user
                String loginJson = String.format(
                    "{\"email\":\"%s\",\"password\":\"password123\"}", 
                    uniqueEmail
                );
                
                HttpRequest loginRequest = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/auth/token"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                        .build();
                
                HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
                
                if (loginResponse.statusCode() == 200) {
                    String token = extractToken(loginResponse.body());
                    
                    if (token != null) {
                        // Try to create a course with user token
                        String json = "{\"name\":\"User Test Course\",\"description\":\"Test\"}";
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(BASE_URL + "/courses"))
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();
                        
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        
                        if (response.statusCode() == 403) {
                            pass(testName, "USER role correctly forbidden from write operations");
                        } else {
                            fail(testName, "USER should get 403, got: " + response.statusCode());
                        }
                    } else {
                        fail(testName, "Could not extract user token");
                    }
                } else {
                    fail(testName, "Could not authenticate as user");
                }
            } else {
                fail(testName, "Could not create test user");
            }
        } catch (Exception e) {
            fail(testName, "Error: " + e.getMessage());
        }
    }
    
    // Helper methods
    private static String extractToken(String jsonResponse) {
        try {
            // Simple JSON parsing for token field
            Pattern pattern = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(jsonResponse);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
    
    private static void pass(String testName, String message) {
        passedTests++;
        results.add("✓ PASS: " + testName + " - " + message);
        System.out.println("✓ PASS: " + testName);
    }
    
    private static void fail(String testName, String message) {
        results.add("✗ FAIL: " + testName + " - " + message);
        System.out.println("✗ FAIL: " + testName);
    }
    
    private static void printResults() {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("TEST RESULTS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        for (String result : results) {
            System.out.println(result);
        }
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("FINAL SCORE");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.printf("Tests Passed: %d / %d%n", passedTests, totalTests);
        System.out.printf("Score: %.2f%%%n", (passedTests * 100.0 / totalTests));
        System.out.println();
        
        if (passedTests == totalTests) {
            System.out.println("🎉 CONGRATULATIONS! All tests passed!");
        } else {
            System.out.println("⚠️  Some tests failed. Please review the requirements.");
        }
        
        System.out.println("=".repeat(80));
    }
}
