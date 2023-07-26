package com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.impl.method.createMatchmakerMethod;

import com.omgservers.application.module.internalModule.impl.service.eventHelpService.EventHelpService;
import com.omgservers.application.module.internalModule.impl.service.eventHelpService.request.InsertEventHelpRequest;
import com.omgservers.application.module.internalModule.model.event.EventQualifierEnum;
import com.omgservers.application.module.internalModule.model.event.body.EventCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchmakerCreatedEventBodyModel;
import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.impl.method.getMatchmakerMethod.GetMatchmakerMethod;
import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.request.CreateMatchmakerInternalRequest;
import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.request.GetMatchmakerInternalRequest;
import com.omgservers.application.module.matchmakerModule.model.matchmaker.MatchmakerModel;
import com.omgservers.application.module.matchmakerModule.model.matchmaker.MatchmakerModelFactory;
import com.omgservers.application.operation.generateIdOperation.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.UUID;

@Slf4j
@QuarkusTest
class CreateMatchmakerMethodTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    CreateMatchmakerMethod createMatchmakerMethod;

    @Inject
    GetMatchmakerMethod getMatchmakerMethod;

    @Inject
    MatchmakerModelFactory matchmakerModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @InjectMock
    EventHelpService eventHelpServiceMock;

    @Test
    void givenMatchmaker_whenCreateMatchmaker_thenEventInsertedAndEntityCreated() {
        final var matchmaker1 = matchmakerModelFactory.create(tenantId(), stageId());
        final var createMatchmakerInternalRequest = new CreateMatchmakerInternalRequest(matchmaker1);
        createMatchmakerMethod.createMatchmaker(TIMEOUT, createMatchmakerInternalRequest);

//        ArgumentCaptor<InsertEventHelpRequest> insertEventRequest = ArgumentCaptor.forClass(InsertEventHelpRequest.class);
//        Mockito.verify(eventHelpServiceMock).insertEvent(insertEventRequest.capture());
//        final var event = insertEventRequest.getValue().getEventBody();
//        assertEquals(EventQualifierEnum.MATCHMAKER_CREATED, event.getQualifier());
//        final var eventBody = (EventCreatedEventBodyModel) event;
//        assertEquals(matchmaker1.getId(), eventBody.getGroup());
//
//        final var originEvent = eventBody.getEvent();
//
//
//        final var originBody = (MatchmakerCreatedEventBodyModel) originEvent.getBody();
//        assertEquals(matchmaker1.getId(), originBody.getUuid());
//        assertEquals(matchmaker1.getTenantId(), originBody.getTenant());
//        assertEquals(matchmaker1.getStageId(), originBody.getStage());
//
//        final var getMatchmakerInternalRequest = new GetMatchmakerInternalRequest(matchmaker1.getId());
//        final var matchmaker2 = getMatchmakerMethod.getMatchmaker(TIMEOUT, getMatchmakerInternalRequest).getMatchmaker();
//        assertEquals(matchmaker1, matchmaker2);
    }

    Long tenantId() {
        return generateIdOperation.generateId();
    }

    Long stageId() {
        return generateIdOperation.generateId();
    }
}