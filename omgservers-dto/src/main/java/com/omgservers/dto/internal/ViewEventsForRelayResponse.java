package com.omgservers.dto.internal;

import com.omgservers.model.eventProjection.EventProjectionModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewEventsForRelayResponse {

    List<EventProjectionModel> eventProjections;
}
