package com.example.demo.map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Component
public class HeremapService {

    private final String apiKey;

    public HeremapService(@Value("${here.maps.apiKey}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDirections(String origin, String destination) {
        final String url = "https://route.ls.hereapi.com/routing/7.2/calculateroute.json"
                + "?apiKey=" + apiKey
                + "&waypoint0=geo!" + origin
                + "&waypoint1=geo!" + destination
                + "&mode=fastest;car;traffic:enabled";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
