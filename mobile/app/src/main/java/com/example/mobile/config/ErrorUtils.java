package com.example.mobile.config;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static String parseError(Response<?> response) {
        Converter<ResponseBody, String> converter =
                ApiClient.getRetrofit()
                        .responseBodyConverter(String.class, new Annotation[0]);
        String error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return "Coś poszło nie tak";
        }
        return error;
    }
}