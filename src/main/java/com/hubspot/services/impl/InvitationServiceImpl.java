package com.hubspot.services.impl;

import com.google.common.base.Stopwatch;
import com.hubspot.dto.Invitation;
import com.hubspot.dto.InvitationList;
import com.hubspot.dto.Partner;
import com.hubspot.dto.PartnersList;
import com.hubspot.exception.InternalServerException;
import com.hubspot.exception.PartnersNotFoundException;
import com.hubspot.services.InvitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

@Service
public class InvitationServiceImpl implements InvitationService {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private static final String GET_URL  = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=51c2e5cbcfdf3c2da4bde6c49740";
    private static final String POST_URL  = "https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=51c2e5cbcfdf3c2da4bde6c49740";

    @Override
    public String getPartnerInfoAndSendInvitation() {

        final Stopwatch timer = Stopwatch.createStarted();

        try {

            //Get the partners information
            HttpHeaders getHttpHeaders = new HttpHeaders();
            getHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<String>(getHttpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PartnersList> getResponse = restTemplate.exchange(GET_URL, HttpMethod.GET, httpEntity, PartnersList.class);

            List<Partner> partnerList;
            if(getResponse.getStatusCode().is2xxSuccessful()) {
                if(getResponse.getBody() != null && !getResponse.getBody().getPartners().isEmpty()) {
                    partnerList = getResponse.getBody().getPartners();
                } else {
                    throw new PartnersNotFoundException("No partners information found");
                }
            } else {
                LOG.error("GetResponse={}",getResponse.getStatusCode());
                throw new InternalServerException("Error fetching the partner list");
            }

            //Parse and post the partners information
            InvitationList invitationList = new InvitationList();
            invitationList.setCountries(getInvites(partnerList));
            MultiValueMap<String, String> postHeaders = new LinkedMultiValueMap<String, String>();
            postHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<InvitationList> request = new HttpEntity<InvitationList>(invitationList, postHeaders);
            ResponseEntity<String> postResponse = restTemplate.exchange(POST_URL, HttpMethod.POST,
                request, String.class, new HashMap<>());

            if(postResponse.getStatusCode().is2xxSuccessful()) {
                return postResponse.getBody();
            } else {
                LOG.error("response={}",postResponse.getStatusCode());
                throw new InternalServerException("Error posting the partner list to send invitations");
            }
        } finally {
            LOG.info("Time taken in ms to getPartnerInfoAndSendInvitation, time={}", timer.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private List<Date> feasibleDatesForPartner(Partner partner) {

        Collections.sort(partner.getAvailableDates());
        Map<Date, Integer> startDates = new TreeMap<Date, Integer>();
        List<Date> sortedDates = partner.getAvailableDates();
        startDates.put(sortedDates.get(0), 0);

        for (int i = 1; i < sortedDates.size() ; i++) {
            Date prevDate = sortedDates.get(i-1);
            Date curDate = sortedDates.get(i);

            long diff = Math.abs(curDate.getTime() - prevDate.getTime());
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays == 1) {
                int count  = startDates.get(prevDate);
                startDates.put(prevDate, count+1);
                startDates.put(curDate, 1);
            }
            startDates.put(curDate, 0);
        }

        List<Date> feasibleDates = new ArrayList<Date>();

        for(Map.Entry<Date, Integer> dateEntry : startDates.entrySet()) {
            if(dateEntry.getValue()  > 0) {
                feasibleDates.add(dateEntry.getKey());
            }
        }

        return feasibleDates;
    }

    private Map<String, List<Partner>> buildCountryDates(List<Partner> partners) {

        Map<String, List<Partner>> countryDates = new HashMap<String, List<Partner>>();
        for(Partner p : partners) {
            List<Partner> partnerList = new ArrayList<Partner>();

            if(countryDates.containsKey(p.getCountry())) {
                partnerList = countryDates.get(p.getCountry());
                partnerList.add(p);
            }
            else {
                partnerList.add(p);
                countryDates.put(p.getCountry(), partnerList);
            }
        }

        return countryDates;
    }

    private Map<String, Map<Date, List<Partner>>> getInvitationList(List<Partner> partnerList) {

        Map<String, List<Partner>> countryMap = buildCountryDates(partnerList);
        Map<String, Map<Date, List<Partner>>> invitatonMap = new HashMap<String, Map<Date, List<Partner>>>();

        for(Map.Entry<String, List<Partner>> countryEntry : countryMap.entrySet()) {
            List<Partner> partners = countryEntry.getValue();
            Set<Date> setDate = new TreeSet<Date>();

            for(Partner p : partners) {
                setDate.addAll(feasibleDatesForPartner(p));
            }

            Map<Date, List<Partner>> partnerMap = new TreeMap<Date, List<Partner>>();
            for(Partner p : partners) {
                List<Date> partnerDate = feasibleDatesForPartner(p);

                for(Date d : partnerDate) {
                    if(setDate.contains(d)) {
                        List<Partner> par = new ArrayList<Partner>();
                        if(partnerMap.containsKey(d)) {
                            par = partnerMap.get(d);
                        }
                        par.add(p);
                        partnerMap.put(d, par);
                    }
                }
            }

            invitatonMap.put(countryEntry.getKey(), partnerMap);
        }

        return invitatonMap;
    }

    private List<Invitation> getInvites(List<Partner> partners) {

        List<Invitation> invites = new ArrayList<Invitation>();
        Map<String, Map<Date, List<Partner>>> invitationMap = getInvitationList(partners);

        for(Map.Entry<String, Map<Date, List<Partner>>> invitationEntry : invitationMap.entrySet()) {
            Map<Date, List<Partner>> dateMap = invitationEntry.getValue();

            int max = -1;
            Date date  = null;
            List<Partner> availablePartnerList = new ArrayList<>();

            for (Map.Entry<Date, List<Partner>> entry : dateMap.entrySet()) {
                List<Partner> partnerList = entry.getValue();

                if(partnerList.size() > max) {
                    max = partnerList.size();
                    date = entry.getKey();
                    availablePartnerList = partnerList;
                }
            }

            Invitation invitation = new Invitation();
            invitation.setAttendeeCount(availablePartnerList.size());

            List<String> emailList  = new ArrayList<String>();
            for(Partner partner : availablePartnerList)
                emailList.add(partner.getEmail());
            invitation.setAttendees(emailList);

            invitation.setName(invitationEntry.getKey());
            invitation.setStartDate(date);
            invites.add(invitation);
        }

        if(!invites.isEmpty()) {
            return invites;
        } else {
            throw new PartnersNotFoundException("No Invites found matching the criteria");
        }
    }

}
