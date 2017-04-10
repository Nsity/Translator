package com.translator.navigation.translate.dictionary;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by nsity on 11.04.17.
 */

public final class JsonUtils {


    private static ObjectMapper objectMapper;

    @Nullable
    public static <T> T deserialize(@NonNull final Class<T> clazz, @NonNull final String json) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (final IOException e) {
           e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static String serialize(final Object target) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(target);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JsonUtils() {
    }
}
