package com.codepath.apps.twitterclient.helpers;

import com.activeandroid.serializer.TypeSerializer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by edwardyang on 5/21/15.
 */
final public class ArrayListSerializer extends TypeSerializer {

    TweetUtilities utils;

    private String delimiter = "~=!=~";

    @Override
    public Class<?> getDeserializedType() {
        return ArrayList.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (data == null) {
            return null;
        }

        return utils.ArrayListToString((ArrayList<String>) data, "", delimiter);
    }

    @Override
    public ArrayList<String> deserialize(Object data) {
        if (data == null) {
            return null;
        }

        String results = (String) data;
        return new ArrayList<>(Arrays.asList(results.split(delimiter)));
    }
}