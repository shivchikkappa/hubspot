package com.hubspot.resources;

import com.hubspot.services.InvitationService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="HubSpot assessement API", description="Rest API for HubSpot assessement")
public class InvitationResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationResource.class);

    @Autowired
    MeterRegistry meterRegistry;

    @Autowired
    InvitationService invitationService;

    @ApiOperation(value = "HubSpot assessement API", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @GetMapping(value = "/invitations", produces = "application/json")
    @ResponseBody
    public ResponseEntity sendInvitation() {
        meterRegistry.counter("sendInvitation Resource").increment();

        String response = invitationService.getPartnerInfoAndSendInvitation();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
