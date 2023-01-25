package com.pos.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pos.gateway.config.EnvelopeProperties;
import com.pos.gateway.config.ServiceDiscoveryProperties;
import com.pos.gateway.soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IdmConsumer {

    private final WebClient webClient;
    private final EnvelopeProperties envelopeProperties;
    private final ServiceDiscoveryProperties discoveryProperties;
    private final XmlMapper xmlMapper;
    @Autowired
    public IdmConsumer(WebClient webClient, EnvelopeProperties envelopeProperties, ServiceDiscoveryProperties discoveryProperties) {
        this.webClient = webClient;
        this.envelopeProperties = envelopeProperties;
        this.discoveryProperties = discoveryProperties;

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        xmlMapper = new XmlMapper(module);
    }

    private String getBodyOfEnvelope(String envelope) {
        return envelope.substring(envelope.indexOf("Body>") + 5, envelope.indexOf("</SOAP-ENV:Body>"));
    }

    private String addBodyToEnvelope(String envelope, String envelopeBody) {
        return envelope.substring(0, envelope.indexOf("Body>") + 5) +
                envelopeBody +
                envelope.substring(envelope.indexOf("</SOAP-ENV:Body>"));
    }

    private String createEnvelopeFromRequest(Object req) throws JsonProcessingException {
        String envelopeBody = xmlMapper.writeValueAsString(req);
        System.out.println(envelopeBody);

        String envelope = addBodyToEnvelope(envelopeProperties.getNoBodyEnvelope(),envelopeBody);
        System.out.println(envelope);
        return envelope;
    }

    private String createEnvelopeFromString(String req) throws JsonProcessingException {
        String envelope = addBodyToEnvelope(envelopeProperties.getNoBodyEnvelope(),req);
        System.out.println(envelope);
        return envelope;
    }

    private String sendEnvelopeToIDM(String envelope) {
        String responseEnvelope = webClient.post().uri(discoveryProperties.getIdmServiceLocation())
                .header("Content-Type","text/xml")
                .bodyValue(envelope)
                .exchangeToMono(client -> {
                    if(client.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return null;
                    }
                    return client.toEntity(String.class);
                }).block()
                .getBody();

        return responseEnvelope;
    }

    public AuthResponse sendAuthRequest(AuthRequest request) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(request);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        responseEnvelope = getBodyOfEnvelope(responseEnvelope);
        System.out.println(responseEnvelope);

        return xmlMapper.readValue(responseEnvelope,AuthResponse.class);
    }

    public LoginResponse sendLoginRequest(LoginRequest request) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(request);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        responseEnvelope = getBodyOfEnvelope(responseEnvelope);
        System.out.println(responseEnvelope);

        return xmlMapper.readValue(responseEnvelope,LoginResponse.class);
    }

    public GetUserResponse getUserById(Integer id) throws JsonProcessingException {
        GetUserRequest request = new GetUserRequest();
        request.setId(id);

        String envelope = createEnvelopeFromRequest(request);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        responseEnvelope = getBodyOfEnvelope(responseEnvelope);
        System.out.println(responseEnvelope);

        return xmlMapper.readValue(responseEnvelope,GetUserResponse.class);
    }

    public GetUsersResponse getUsers() throws JsonProcessingException {
        String envelope = createEnvelopeFromString("<w:getUsersRequest></w:getUsersRequest>");
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        responseEnvelope = getBodyOfEnvelope(responseEnvelope);
        System.out.println(responseEnvelope);

        return xmlMapper.readValue(responseEnvelope,GetUsersResponse.class);
    }

    public void createUser(CreateUserRequest createUserRequest) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(createUserRequest);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        //responseEnvelope = getBodyOfEnvelope(responseEnvelope);
        System.out.println(responseEnvelope);
    }

    public void updateUser(UpdateUserRequest request) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(request);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        System.out.println(responseEnvelope);
    }

    public void deleteUser(DeleteUserRequest request) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(request);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        System.out.println(responseEnvelope);
    }

    public void sendLogoutRequest(LogoutRequest logoutRequest) throws JsonProcessingException {
        String envelope = createEnvelopeFromRequest(logoutRequest);
        String responseEnvelope = sendEnvelopeToIDM(envelope);
        System.out.println(responseEnvelope);
    }
}
