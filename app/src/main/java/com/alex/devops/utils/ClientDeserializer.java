package com.alex.devops.utils;

import com.alex.devops.db.Client;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ClientDeserializer extends StdDeserializer<Client> {
    public ClientDeserializer() {
        this(null);
    }

    protected ClientDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Client deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int id = node.get("id").intValue();
        long createDate = node.get("createDate").longValue();
        String childName = node.get("childName").asText();
        long childBirthDay = node.get("childBirthDay").longValue();

        String mainParentFirstName = node.get("mainParentFirstName").asText();
        String mainSecondName = node.get("mainSecondName").asText();
        String mainPatronymicName = node.get("mainPatronymicName").asText();
        String mainPhoneNumber = node.get("mainPhoneNumber").asText();
        byte[] mainBlobPhoto = node.get("mainBlobPhoto").binaryValue();

        String secondParentFirstName = node.get("secondParentFirstName").asText();
        String secondParentSecondName = node.get("secondParentSecondName").asText();
        String secondPatronymicName = node.get("secondPatronymicName").asText();
        String secondPhoneNumber = node.get("secondPhoneNumber").asText();
        byte[] secondPhotoBlob = node.get("secondPhotoBlob").binaryValue();

        long lastVisit = node.get("lastVisit").longValue();
        int visitCounter = node.get("visitCounter").intValue();


        Client client = new Client();
//        client.setId(id);
        client.setCreateDate(createDate);
        client.setChildName(childName);
        client.setChildBirthDay(childBirthDay);

        client.setMainParentFirstName(mainParentFirstName);
        client.setMainSecondName(mainSecondName);
        client.setMainPatronymicName(mainPatronymicName);
        client.setMainPhoneNumber(mainPhoneNumber);
        client.setMainBlobPhoto(mainBlobPhoto);

        client.setSecondParentFirstName(secondParentFirstName);
        client.setSecondParentSecondName(secondParentSecondName);
        client.setSecondPatronymicName(secondPatronymicName);
        client.setSecondPhoneNumber(secondPhoneNumber);
        client.setSecondPhotoBlob(secondPhotoBlob);

        client.setLastVisit(lastVisit);
        client.setVisitCounter(visitCounter);

        return client;
    }
}
