package com.omgservers.service.service.room.impl.method.handleTextMessage;

import com.omgservers.service.service.room.dto.HandleTextMessageRequest;
import io.smallrye.mutiny.Uni;

public interface HandleTextMessageMethod {
    Uni<Void> handleTextMessage(HandleTextMessageRequest request);
}
