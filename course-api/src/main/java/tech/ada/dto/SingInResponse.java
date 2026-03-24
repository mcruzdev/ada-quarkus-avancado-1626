package tech.ada.dto;

public record SingInResponse(
        String token,
        Long expiresIn
) {

}
