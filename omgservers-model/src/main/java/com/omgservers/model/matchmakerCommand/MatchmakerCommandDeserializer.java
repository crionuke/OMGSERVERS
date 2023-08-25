package com.omgservers.model.matchmakerCommand;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

public class MatchmakerCommandDeserializer extends StdDeserializer<MatchmakerCommandModel> {

    public MatchmakerCommandDeserializer() {
        this(null);
    }

    public MatchmakerCommandDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MatchmakerCommandModel deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JacksonException {
        try {
            return deserialize(parser);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    MatchmakerCommandModel deserialize(JsonParser parser) throws IOException {
        final var mapper = (ObjectMapper) parser.getCodec();
        final var root = (JsonNode) mapper.readTree(parser);

        final var commandModel = new MatchmakerCommandModel();

        final var idNode = root.get("id");
        if (idNode != null) {
            commandModel.setId(Long.valueOf(idNode.asText()));
        }

        final var runtimeIdNode = root.get("runtimeId");
        if (runtimeIdNode != null) {
            commandModel.setRuntimeId(Long.valueOf(runtimeIdNode.asText()));
        }

        final var createdNode = root.get("created");
        if (createdNode != null) {
            commandModel.setCreated(Instant.parse(createdNode.asText()));
        }

        final var modifiedNode = root.get("modified");
        if (modifiedNode != null) {
            commandModel.setModified(Instant.parse(modifiedNode.asText()));
        }

        final var qualifierNode = root.get("qualifier");
        if (qualifierNode != null) {
            final var qualifier = MatchmakerCommandQualifierEnum.valueOf(qualifierNode.asText());
            commandModel.setQualifier(qualifier);

            final var bodyNode = root.get("body");
            if (bodyNode != null) {
                final var body = mapper.treeToValue(bodyNode, qualifier.getBodyClass());
                commandModel.setBody(body);
            }
        }

        final var statusNode = root.get("status");
        if (statusNode != null) {
            final var status = MatchmakerCommandStatusEnum.valueOf(statusNode.asText());
            commandModel.setStatus(status);
        }

        return commandModel;
    }
}
