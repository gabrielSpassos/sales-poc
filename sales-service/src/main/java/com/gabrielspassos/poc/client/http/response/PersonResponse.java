package com.gabrielspassos.poc.client.http.response;

import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import lombok.Data;

@Data
public class PersonResponse {

    private PersonValidationStatusEnum status;

}
