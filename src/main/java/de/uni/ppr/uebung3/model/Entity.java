package de.uni.ppr.uebung3.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {


    protected final String id;

    protected Entity(String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }




    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", getClass().getSimpleName());
        return json;
    }







    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + id + "'}";
    }


    public Map<String, Object> toNode() {
        Map<String, Object> props = new HashMap<>();
        props.put("id", id);
        props.put("type", getClass().getSimpleName());
        return props;
    }
}
