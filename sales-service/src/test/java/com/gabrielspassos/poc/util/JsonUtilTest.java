package com.gabrielspassos.poc.util;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.SaleStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {

    private static final String SALE_ID = "dfsg73462bdsaf";
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final String BIRTH_DATE = "1998-05-10";
    private static final String JSON = "{\"id\":\"dfsg73462bdsaf\",\"person\":{\"nationalIdentificationNumber\":\"94206237000\",\"birthdate\":\"1998-05-10\",\"firstName\":\"Jose\",\"lastName\":\"Souza\",\"email\":\"jose.souza@gmail.com\"}}";

    @Test
    public void shouldReturnObjectAsJsonString() {
        PersonEvent personEvent = PersonStub.createEvent(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEvent saleEvent = SaleStub.createEvent(SALE_ID, personEvent);

        String stringJson = JsonUtil.getStringJson(saleEvent);

        assertEquals(JSON, stringJson);
    }

}