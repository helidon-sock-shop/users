/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.users;

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.helidon.examples.sockshop.users.JsonHelpers.embed;
import static io.helidon.examples.sockshop.users.JsonHelpers.obj;

@ApplicationScoped
@Path("/cards")
public class CardsResource implements CardApi{

    @Inject
    private UserRepository users;

    @Override
    public Response getAllCards() {
        return Response.ok(embed("card", Collections.emptyList())).build();
    }

    @Override
    public Response registerCard(AddCardRequest req) {
        Card card = new Card(req.longNum, req.expires, req.ccv);
        CardId id = users.addCard(req.userID, card);

        return Response.ok(obj().add("id", id.toString()).build()).build();
    }

    @Override
    public Card getCard(CardId id) {
        return users.getCard(id).mask();
    }

    @Override
    public Response deleteCard(CardId id) {
        try {
            users.removeCard(id);
            return status(true);
        }
        catch (RuntimeException e) {
            return status(false);
        }
    }

    // --- helpers ----------------------------------------------------------

    private static Response status(boolean fSuccess) {
        return Response.ok(obj().add("status", fSuccess).build()).build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCardRequest {
        public String longNum;
        public String expires;
        public String ccv;
        public String userID;
    }
}
