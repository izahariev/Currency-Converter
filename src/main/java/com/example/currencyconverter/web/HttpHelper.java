package com.example.currencyconverter.web;

import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Class providing generic methods for working with HTTP
 *
 * @author Ivaylo Zahariev
 */
public class HttpHelper {

    /**
     * Sends an HTTP request to the given URL using the given method and adding the given parameters
     * @param uri
     *  The URI
     * @param httpMethod
     *  The HTTP method
     * @param parameters
     *  The parameters
     * @param headers
     *  Request headers
     * @param bodyPublisher
     *  Body for POST requests
     * @return
     *  The response body as a String
     */
    public static HttpResponse<String> sendRequest(String uri, HttpMethod httpMethod, Map<String, String> parameters,
                                                   Map<String, String> headers, HttpRequest.BodyPublisher bodyPublisher) {
        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(uri);
            if (parameters != null) {
                parameters.forEach(uriComponentsBuilder::queryParam);
            }
            HttpRequest.Builder requestBuilder = null;
            switch (httpMethod.toString()) {
                case "GET" -> {
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(uriComponentsBuilder.toUriString()))
                            .GET();
                }
                case "POST" -> {
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(uriComponentsBuilder.toUriString()))
                            .POST(bodyPublisher == null ? HttpRequest.BodyPublishers.noBody() : bodyPublisher);
                }
            }

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    requestBuilder.header(header.getKey(), header.getValue());
                }
            }

            return HttpClient.newHttpClient().send(requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
