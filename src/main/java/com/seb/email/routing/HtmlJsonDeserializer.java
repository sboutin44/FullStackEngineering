package com.seb.email.routing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlJsonDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String html = jp.getText(); // Parse the json as a string,
        Document doc = Jsoup.parse(html); // Convert the html as a plaintext Document object
        return doc.text();
    }
}
