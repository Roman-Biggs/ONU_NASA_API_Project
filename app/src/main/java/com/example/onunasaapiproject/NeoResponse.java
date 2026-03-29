package com.example.onunasaapiproject;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class NeoResponse {

    private Links links;
    private Integer elementCount;

    // Key (String) - contains data (for example, "2026-03-29")
    // Value (List<AsteroidInfo>) - list of all asteroids at this date
    @SerializedName("near_earth_objects")
    private Map<String, List<AsteroidInfo>> nearEarthObjects;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Integer getElementCount() {
        return elementCount;
    }

    public void setElementCount(Integer elementCount) {
        this.elementCount = elementCount;
    }

    public Map<String, List<AsteroidInfo>> getNearEarthObjects() {
        return nearEarthObjects;
    }

    public void setNearEarthObjects(Map<String, List<AsteroidInfo>> nearEarthObjects) {
        this.nearEarthObjects = nearEarthObjects;
    }

}