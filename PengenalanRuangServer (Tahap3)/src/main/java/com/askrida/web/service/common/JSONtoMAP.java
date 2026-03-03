package com.askrida.web.service.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONtoMAP { 
	
	
	public static DTOMap jsonToMap(JSONObject json) throws JSONException {
		DTOMap retMap = new DTOMap();
	    if(json != JSONObject.NULL) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}
	
	
	public static DTOMap toMap(JSONObject object) throws JSONException {
	   DTOMap map = new DTOMap();
	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value =  !object.isNull(key) ? object.get(key): null;

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<DTOMap> toList(JSONArray array) throws JSONException {
	    List<DTOMap> list = new ArrayList<DTOMap>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        DTOMap dto = new DTOMap();
	        if(value instanceof JSONArray) {
	        	dto = (DTOMap) toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	        	dto = toMap((JSONObject) value);
	        }
	        list.add(dto);
	    }
	    return list;
	}
}