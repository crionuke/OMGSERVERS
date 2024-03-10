package com.omgservers.service.exception;

public enum ExceptionQualifierEnum {
    RAW_TOKEN_WRONG,
    USER_TOKEN_WRONG,
    OBJECT_WRONG,
    MESSAGE_QUALIFIER_WRONG,
    CLIENT_ID_WRONG,
    RUNTIME_ID_WRONG,
    STAGE_SECRET_WRONG,
    CONFIG_JSON_WRONG,
    VERSION_FILE_WRONG,
    CREDENTIALS_WRONG,
    CONFIGURATION_WRONG,
    SHARD_WRONG,

    CONFIG_JSON_NOT_FOUND,
    COMMAND_EXECUTOR_NOT_FOUND,
    OBJECT_NOT_FOUND,
    PARENT_NOT_FOUND,
    PERMISSION_NOT_FOUND,
    DOCKER_CONTAINER_NOT_FOUND,
    CLIENT_NOT_FOUND,
    LOBBY_NOT_FOUND,
    RUNTIME_NOT_FOUND,
    VERSION_NOT_FOUND,
    MATCHMAKER_NOT_FOUND,

    CLIENT_MESSAGE_BODY_TYPE_MISMATCH,
    MATCHMAKER_MESSAGE_BODY_TYPE_MISMATCH,
    COMMAND_BODY_TYPE_MISMATCH,
    TOKEN_HASH_MISMATCH,

    DB_VIOLATION,
    DB_EXCEPTION,
    DB_DATA_CORRUPTED,
    SHARD_LOCKED,
    ID_GENERATOR_FAILED,
    ILLEGAL_ARGUMENT,
    CONSTRAINT_VIOLATION,
    INTERNAL_EXCEPTION,
}
