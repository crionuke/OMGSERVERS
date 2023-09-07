package com.omgservers.model.message;

import com.omgservers.model.message.body.CredentialsMessageBodyModel;
import com.omgservers.model.message.body.EventMessageBodyModel;
import com.omgservers.model.message.body.MatchMessageBodyModel;
import com.omgservers.model.message.body.MatchmakerMessageBodyModel;
import com.omgservers.model.message.body.SignInMessageBodyModel;
import com.omgservers.model.message.body.SignUpMessageBodyModel;

public enum MessageQualifierEnum {
    SIGN_IN_MESSAGE(SignInMessageBodyModel.class),
    SIGN_UP_MESSAGE(SignUpMessageBodyModel.class),
    MATCHMAKER_MESSAGE(MatchmakerMessageBodyModel.class),
    CREDENTIALS_MESSAGE(CredentialsMessageBodyModel.class),
    EVENT_MESSAGE(EventMessageBodyModel.class),
    MATCH_MESSAGE(MatchMessageBodyModel.class);

    final Class<? extends MessageBodyModel> bodyClass;

    MessageQualifierEnum(Class<? extends MessageBodyModel> bodyClass) {
        this.bodyClass = bodyClass;
    }

    public Class<? extends MessageBodyModel> getBodyClass() {
        return bodyClass;
    }
}
