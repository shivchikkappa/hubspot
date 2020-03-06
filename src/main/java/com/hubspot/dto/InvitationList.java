package com.hubspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvitationList {

    @ApiModelProperty(notes = "Attendee list to send invitation")
    private List<Invitation> countries;

    public List<Invitation> getCountries() {
        return countries;
    }

    public void setCountries(List<Invitation> countries) {
        this.countries = countries;
    }

    @Override
    public String toString() {
        return "Countries{" +
            "countries=" + countries +
            '}';
    }
}
