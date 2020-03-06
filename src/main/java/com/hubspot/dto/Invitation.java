package com.hubspot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Invitation {

    @ApiModelProperty(notes = "Count of attendees")
    private int attendeeCount;

    @ApiModelProperty(notes = "List of attendees")
    private List<String> attendees;

    @ApiModelProperty(notes = "name of attendee")
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(notes = "StartDate of attendee")
    private Date startDate;

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "attendeeCount=" + attendeeCount +
            ", attendees=" + attendees +
            ", name='" + name + '\'' +
            ", startDate=" + startDate +
            '}';
    }
}
