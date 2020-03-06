package com.hubspot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Partner {

    @ApiModelProperty(notes = "Partner first name")
    private String firstName;

    @ApiModelProperty(notes = "Partner last name")
    private String lastName;

    @ApiModelProperty(notes = "Partner email")
    private String email;

    @ApiModelProperty(notes = "Partner country")
    private String country;

    @ApiModelProperty(notes = "Partner available dates")
    private List<Date> availableDates;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Date> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<Date> availableDates) {
        this.availableDates = availableDates;
    }

    @Override
    public String toString() {
        return "Partners{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", country='" + country + '\'' +
            ", availableDates=" + availableDates +
            '}';
    }
}
