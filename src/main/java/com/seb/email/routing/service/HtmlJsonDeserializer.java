package com.seb.email.routing.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        /* Give an pretty-printing feature to jackson's JsonParser. */

        String html = jp.getText();         // Parse the json as a string,
        Document doc = Jsoup.parse(html);   // Convert the html as a plaintext Document object
        return doc.text();
    }
}
