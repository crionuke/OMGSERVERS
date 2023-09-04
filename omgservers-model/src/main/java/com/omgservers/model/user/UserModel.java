package com.omgservers.model.user;

import com.omgservers.exception.ServerSideBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    public static void validate(UserModel user) {
        if (user == null) {
            throw new ServerSideBadRequestException("user is null");
        }
        validateId(user.getId());
        validateCreated(user.getCreated());
        validateModified(user.getModified());
        validateRole(user.getRole());
        validatePasswordHash(user.getPasswordHash());
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new ServerSideBadRequestException("id field is null");
        }
    }

    public static void validateCreated(Instant created) {
        if (created == null) {
            throw new ServerSideBadRequestException("created field is null");
        }
    }

    public static void validateModified(Instant modified) {
        if (modified == null) {
            throw new ServerSideBadRequestException("modified field is null");
        }
    }

    public static void validateRole(UserRoleEnum role) {
        if (role == null) {
            throw new ServerSideBadRequestException("role field is null");
        }
    }

    public static void validatePasswordHash(String passwordHash) {
        if (passwordHash == null) {
            throw new ServerSideBadRequestException("passwordHash field is null");
        }
        if (passwordHash.isBlank()) {
            throw new ServerSideBadRequestException("passwordHash string is blank");
        }
        if (passwordHash.length() > 128) {
            throw new ServerSideBadRequestException("passwordHash string is too long");
        }
    }

    Long id;
    Instant created;
    Instant modified;
    UserRoleEnum role;
    @ToString.Exclude
    String passwordHash;
}
