package com.omgservers.model.matchCommand;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

public class MatchCommandDeserializer extends StdDeserializer<MatchCommandModel> {

    public MatchCommandDeserializer() {
        this(null);
    }

    public MatchCommandDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MatchCommandModel deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JacksonException {
        try {
            return deserialize(parser);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    MatchCommandModel deserialize(JsonParser parser) throws IOException {
        final var mapper = (ObjectMapper) parser.getCodec();
        final var root = (JsonNode) mapper.readTree(parser);

        final var commandModel = new MatchCommandModel();

        final var idNode = root.get("id");
        if (idNode != null) {
            commandModel.setId(Long.valueOf(idNode.asText()));
        }

        final var matchmakerIdNode = root.get("matchmakerId");
        if (matchmakerIdNode != null) {
            commandModel.setMatchmakerId(Long.valueOf(matchmakerIdNode.asText()));
        }

        final var matchIdNode = root.get("matchId");
        if (matchIdNode != null) {
            commandModel.setMatchId(Long.valueOf(matchIdNode.asText()));
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
            final var qualifier = MatchCommandQualifierEnum.valueOf(qualifierNode.asText());
            commandModel.setQualifier(qualifier);

            final var bodyNode = root.get("body");
            if (bodyNode != null) {
                final var body = mapper.treeToValue(bodyNode, qualifier.getBodyClass());
                commandModel.setBody(body);
            }
        }

        return commandModel;
    }
}
