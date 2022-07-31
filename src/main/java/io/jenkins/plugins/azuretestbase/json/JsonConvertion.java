package io.jenkins.plugins.azuretestbase.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import edu.umd.cs.findbugs.annotations.NonNull;

public final class JsonConvertion {
    public JsonConvertion() {
        throw new RuntimeException("This is a static factory class, don't initialize it!");
    }


    // convert object to jsonStr
    public static <T> String objToJson(@NonNull T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // do not throw exception if there is no filter specified
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        String result = mapper.writeValueAsString(obj);
        return result;
    }


    public static <T> String objToJson(@NonNull T obj, @NonNull FilterProvider filterProvider) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setFilterProvider(filterProvider);
        String result = mapper.writeValueAsString(obj);
        return result;
    }


    // convert jsonStr to generic object through Class
    public static <T> T jsonToObj(@NonNull String jsonStr, @NonNull Class<?> containerClass, @NonNull Class<?> elementClass) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(containerClass, elementClass);
        T result = mapper.readValue(jsonStr, javaType);
        return result;
    }


    // convert jsonStr to object through TypeReference
    public static <T> T jsonToObj(@NonNull String jsonStr, @NonNull TypeReference<T> cls) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T result = mapper.readValue(jsonStr, cls);
        return result;
    }


    // convert jsonStr to object through Class
    public static <T> T jsonToObj(@NonNull String jsonStr, @NonNull Class<T> cls) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T result = mapper.readValue(jsonStr, cls);
        return result;
    }
}
