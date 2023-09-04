package prosense.sassa.pipclient;

import javax.xml.datatype.DatatypeFactory;

import java.util.GregorianCalendar;

public class Test {

    public static void main(String args[]) throws Exception {
        BizTalkServiceInstance service = new BizTalkServiceInstance();
        PersonIdentiyService port = service.getBasicHttpBindingITwoWayAsync();
        ObjectFactory factory = new ObjectFactory();

        PersonIdentityProfileRequest request = factory.createPersonIdentityProfileRequest();
        request.setTypeId("000063");

        // Header
        MessageIdType messageId = factory.createMessageIdType();
        messageId.setMessageNumber("b9e08d67-8e60-4598-b9c4-71ce695a525b");
        IJSMessageSourceCodeType messageSource = factory.createIJSMessageSourceCodeType();
        messageSource.setName("IJSMessageSource");
        messageSource.setType(ReferenceDataTypeType3.ICS_REFDATA);
        messageSource.setVersion("4.0");
        messageSource.setValue("PIVASASSA");
        messageId.setMessageSource(messageSource);

        MessageInfoType messageInfo = factory.createMessageInfoType();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        messageInfo.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        messageInfo.setMessageId(messageId);

        UserMessageType userMessage = factory.createUserMessageType();
        userMessage.setMessageInfo(messageInfo);

        MessageHeaderType header = factory.createMessageHeaderType();
        header.setUserMessage(userMessage);
        request.setHeader(header);

        // Payload
        MessagePayloadTypeRq payload = factory.createMessagePayloadTypeRq();
        payload.setSANationalID("8901166311184");
        request.setPayload(payload);

        try {
            PersonIdentityProfileResponse response = port.opPersonIdentiyService(request);
            System.out.println(response.getPayloadRs().getDHA_PersonIdentityProfile().getPersonIdentityProfile().get(0).getPersonName().getPersonGivenName());
            System.out.println(response.getPayloadRs().getDHA_PersonIdentityProfile().getPersonIdentityProfile().get(0).getPersonBirthDate());
        } catch (PersonIdentiyServiceOpPersonIdentiyServiceErrorMessage error) {
            System.out.println(error.getFaultInfo().getError().getDHAError().get(0).getErrorCode());
        }
    }
}
