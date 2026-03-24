//package tech.ada.resource;
//
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import tech.ada.security.JwtGenerator;
//
//class AuthResourceTest {
//
//
//    @Test
//    void testGenerateJws() {
//        // arrange
//
//        JwtGenerator mock = Mockito.mock(JwtGenerator.class);
//
//        Mockito.when(mock.generateJws(userAuth.username, userAuth.role)).thenReturn("mocked-jws-token");
//
//        AuthResource authResource = new AuthResource(
//                mock
//        );
//
//        // action
//        Response response = authResource.generateJws();
//
//        // assert
//        // expected
//
//        String token = response.readEntity(String.class);
//
//        Assertions.assertNotNull(token);
//    }
//}