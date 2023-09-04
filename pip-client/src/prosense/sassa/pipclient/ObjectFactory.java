package prosense.sassa.pipclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the prosense.sassa.pipclient package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PersonResidentialAddress_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonResidentialAddress");
    private final static QName _ErrorCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "ErrorCode");
    private final static QName _AddressLineText_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "AddressLineText");
    private final static QName _RefToMessageId_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "RefToMessageId");
    private final static QName _PersonBirthCountryCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonBirthCountryCode");
    private final static QName _IdentityDocumentType_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "IdentityDocumentType");
    private final static QName _DHAError_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "DHA_Error");
    private final static QName _PayloadRs_QNAME = new QName("http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Response/1.0", "PayloadRs");
    private final static QName _LocationPostalCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "LocationPostalCode");
    private final static QName _PersonDeathDate_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonDeathDate");
    private final static QName _PersonFacialImage_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonFacialImage");
    private final static QName _PersonMaritalStatusCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonMaritalStatusCode");
    private final static QName _PersonSurname_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonSurname");
    private final static QName _MessageProperty_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageProperty");
    private final static QName _PersonMaidenName_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonMaidenName");
    private final static QName _Payload_QNAME = new QName("http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0", "Payload");
    private final static QName _SANationalID_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "SANationalID");
    private final static QName _MessageNumber_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageNumber");
    private final static QName _PersonLivingIndicator_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonLivingIndicator");
    private final static QName _PersonName_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonName");
    private final static QName _Timestamp_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "Timestamp");
    private final static QName _UserMessage_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "UserMessage");
    private final static QName _LocationProvinceCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "LocationProvinceCode");
    private final static QName _PersonBirthDate_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonBirthDate");
    private final static QName _Header_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "Header");
    private final static QName _DHATransaction_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "DHA_Transaction");
    private final static QName _IdentityDocumentCountryCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "IdentityDocumentCountryCode");
    private final static QName _PersonIdentification_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonIdentification");
    private final static QName _ContactTypeCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "ContactTypeCode");
    private final static QName _PersonMaritalTypeCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonMaritalTypeCode");
    private final static QName _MessageId_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageId");
    private final static QName _PersonContactInformation_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonContactInformation");
    private final static QName _PersonPostalAddress_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonPostalAddress");
    private final static QName _PersonMarriageDate_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonMarriageDate");
    private final static QName _BinaryObjectBase64_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "BinaryObject_Base64");
    private final static QName _MessageData_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageData");
    private final static QName _ContactDetailsText_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "ContactDetailsText");
    private final static QName _TransactionDateTime_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "TransactionDateTime");
    private final static QName _TransactionID_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "TransactionID");
    private final static QName _IdentityDocumentNumber_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "IdentityDocumentNumber");
    private final static QName _PersonGenderCode_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonGenderCode");
    private final static QName _PersonIdentityProfile_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonIdentityProfile");
    private final static QName _PersonGivenName_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "PersonGivenName");
    private final static QName _MessageInfo_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageInfo");
    private final static QName _DHAPersonIdentityProfile_QNAME = new QName("http://ijs.gov.za/schemas/sajxdm/2.0.0", "DHA_PersonIdentityProfile");
    private final static QName _MessageSource_QNAME = new QName("http://ijs.gov.za/schemas/sajsip/3.0.0", "MessageSource");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: prosense.sassa.pipclient1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersonIdentityProfileResponseErrorType }
     * 
     */
    public PersonIdentityProfileResponseErrorType createPersonIdentityProfileResponseErrorType() {
        return new PersonIdentityProfileResponseErrorType();
    }

    /**
     * Create an instance of {@link PersonGenderCodeType }
     * 
     */
    public PersonGenderCodeType createPersonGenderCodeType() {
        return new PersonGenderCodeType();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileType }
     * 
     */
    public PersonIdentityProfileType createPersonIdentityProfileType() {
        return new PersonIdentityProfileType();
    }

    /**
     * Create an instance of {@link DHAPersonIdentityProfileType }
     * 
     */
    public DHAPersonIdentityProfileType createDHAPersonIdentityProfileType() {
        return new DHAPersonIdentityProfileType();
    }

    /**
     * Create an instance of {@link ContactTypeCodeType }
     * 
     */
    public ContactTypeCodeType createContactTypeCodeType() {
        return new ContactTypeCodeType();
    }

    /**
     * Create an instance of {@link MaritalTypeCodeType }
     * 
     */
    public MaritalTypeCodeType createMaritalTypeCodeType() {
        return new MaritalTypeCodeType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link ContactInformationType }
     * 
     */
    public ContactInformationType createContactInformationType() {
        return new ContactInformationType();
    }

    /**
     * Create an instance of {@link DHATransactionType }
     * 
     */
    public DHATransactionType createDHATransactionType() {
        return new DHATransactionType();
    }

    /**
     * Create an instance of {@link CountriesCodeType }
     * 
     */
    public CountriesCodeType createCountriesCodeType() {
        return new CountriesCodeType();
    }

    /**
     * Create an instance of {@link PersonAssignedIdentityType }
     * 
     */
    public PersonAssignedIdentityType createPersonAssignedIdentityType() {
        return new PersonAssignedIdentityType();
    }

    /**
     * Create an instance of {@link SouthAfricanProvincesCodeType }
     * 
     */
    public SouthAfricanProvincesCodeType createSouthAfricanProvincesCodeType() {
        return new SouthAfricanProvincesCodeType();
    }

    /**
     * Create an instance of {@link PersonNameType }
     * 
     */
    public PersonNameType createPersonNameType() {
        return new PersonNameType();
    }

    /**
     * Create an instance of {@link IdentityDocumentTypeCodeType }
     * 
     */
    public IdentityDocumentTypeCodeType createIdentityDocumentTypeCodeType() {
        return new IdentityDocumentTypeCodeType();
    }

    /**
     * Create an instance of {@link DHAErrorType }
     * 
     */
    public DHAErrorType createDHAErrorType() {
        return new DHAErrorType();
    }

    /**
     * Create an instance of {@link ImageType }
     * 
     */
    public ImageType createImageType() {
        return new ImageType();
    }

    /**
     * Create an instance of {@link MaritalStatusCodeType }
     * 
     */
    public MaritalStatusCodeType createMaritalStatusCodeType() {
        return new MaritalStatusCodeType();
    }

    /**
     * Create an instance of {@link ErrorCodeDHACodeType }
     * 
     */
    public ErrorCodeDHACodeType createErrorCodeDHACodeType() {
        return new ErrorCodeDHACodeType();
    }

    /**
     * Create an instance of {@link TransactionType }
     * 
     */
    public TransactionType createTransactionType() {
        return new TransactionType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link BinaryType }
     * 
     */
    public BinaryType createBinaryType() {
        return new BinaryType();
    }

    /**
     * Create an instance of {@link SuperType }
     * 
     */
    public SuperType createSuperType() {
        return new SuperType();
    }

    /**
     * Create an instance of {@link ReferenceDataType }
     * 
     */
    public ReferenceDataType createReferenceDataType() {
        return new ReferenceDataType();
    }

    /**
     * Create an instance of {@link MessageIdType }
     * 
     */
    public MessageIdType createMessageIdType() {
        return new MessageIdType();
    }

    /**
     * Create an instance of {@link MessageHeaderType }
     * 
     */
    public MessageHeaderType createMessageHeaderType() {
        return new MessageHeaderType();
    }

    /**
     * Create an instance of {@link MessageHeaderDataType }
     * 
     */
    public MessageHeaderDataType createMessageHeaderDataType() {
        return new MessageHeaderDataType();
    }

    /**
     * Create an instance of {@link MessagePropertyType }
     * 
     */
    public MessagePropertyType createMessagePropertyType() {
        return new MessagePropertyType();
    }

    /**
     * Create an instance of {@link IJSMessageSourceCodeType }
     * 
     */
    public IJSMessageSourceCodeType createIJSMessageSourceCodeType() {
        return new IJSMessageSourceCodeType();
    }

    /**
     * Create an instance of {@link UserMessageType }
     * 
     */
    public UserMessageType createUserMessageType() {
        return new UserMessageType();
    }

    /**
     * Create an instance of {@link MessageInfoType }
     * 
     */
    public MessageInfoType createMessageInfoType() {
        return new MessageInfoType();
    }

    /**
     * Create an instance of {@link MessagePayloadType3 }
     * 
     */
    public MessagePayloadType3 createMessagePayloadType3() {
        return new MessagePayloadType3();
    }

    /**
     * Create an instance of {@link ResponseMessageType }
     * 
     */
    public ResponseMessageType createResponseMessageType() {
        return new ResponseMessageType();
    }

    /**
     * Create an instance of {@link ReferenceDataType3 }
     * 
     */
    public ReferenceDataType3 createReferenceDataType3() {
        return new ReferenceDataType3();
    }

    /**
     * Create an instance of {@link RequestMessageType }
     * 
     */
    public RequestMessageType createRequestMessageType() {
        return new RequestMessageType();
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link MessageErrorType }
     * 
     */
    public MessageErrorType createMessageErrorType() {
        return new MessageErrorType();
    }

    /**
     * Create an instance of {@link ResponseErrorMessageType }
     * 
     */
    public ResponseErrorMessageType createResponseErrorMessageType() {
        return new ResponseErrorMessageType();
    }

    /**
     * Create an instance of {@link MessagePayloadTypeRq }
     * 
     */
    public MessagePayloadTypeRq createMessagePayloadTypeRq() {
        return new MessagePayloadTypeRq();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileRequest }
     * 
     */
    public PersonIdentityProfileRequest createPersonIdentityProfileRequest() {
        return new PersonIdentityProfileRequest();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileRequestType }
     * 
     */
    public PersonIdentityProfileRequestType createPersonIdentityProfileRequestType() {
        return new PersonIdentityProfileRequestType();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileResponse }
     * 
     */
    public PersonIdentityProfileResponse createPersonIdentityProfileResponse() {
        return new PersonIdentityProfileResponse();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileResponseType }
     * 
     */
    public PersonIdentityProfileResponseType createPersonIdentityProfileResponseType() {
        return new PersonIdentityProfileResponseType();
    }

    /**
     * Create an instance of {@link MessagePayloadTypeRs }
     * 
     */
    public MessagePayloadTypeRs createMessagePayloadTypeRs() {
        return new MessagePayloadTypeRs();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileResponseError }
     * 
     */
    public PersonIdentityProfileResponseError createPersonIdentityProfileResponseError() {
        return new PersonIdentityProfileResponseError();
    }

    /**
     * Create an instance of {@link PersonIdentityProfileResponseErrorType.Error }
     * 
     */
    public PersonIdentityProfileResponseErrorType.Error createPersonIdentityProfileResponseErrorTypeError() {
        return new PersonIdentityProfileResponseErrorType.Error();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonResidentialAddress")
    public JAXBElement<AddressType> createPersonResidentialAddress(AddressType value) {
        return new JAXBElement<AddressType>(_PersonResidentialAddress_QNAME, AddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorCodeDHACodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "ErrorCode")
    public JAXBElement<ErrorCodeDHACodeType> createErrorCode(ErrorCodeDHACodeType value) {
        return new JAXBElement<ErrorCodeDHACodeType>(_ErrorCode_QNAME, ErrorCodeDHACodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "AddressLineText")
    public JAXBElement<String> createAddressLineText(String value) {
        return new JAXBElement<String>(_AddressLineText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "RefToMessageId")
    public JAXBElement<MessageIdType> createRefToMessageId(MessageIdType value) {
        return new JAXBElement<MessageIdType>(_RefToMessageId_QNAME, MessageIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountriesCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonBirthCountryCode")
    public JAXBElement<CountriesCodeType> createPersonBirthCountryCode(CountriesCodeType value) {
        return new JAXBElement<CountriesCodeType>(_PersonBirthCountryCode_QNAME, CountriesCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentityDocumentTypeCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "IdentityDocumentType")
    public JAXBElement<IdentityDocumentTypeCodeType> createIdentityDocumentType(IdentityDocumentTypeCodeType value) {
        return new JAXBElement<IdentityDocumentTypeCodeType>(_IdentityDocumentType_QNAME, IdentityDocumentTypeCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DHAErrorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "DHA_Error")
    public JAXBElement<DHAErrorType> createDHAError(DHAErrorType value) {
        return new JAXBElement<DHAErrorType>(_DHAError_QNAME, DHAErrorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessagePayloadTypeRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Response/1.0", name = "PayloadRs")
    public JAXBElement<MessagePayloadTypeRs> createPayloadRs(MessagePayloadTypeRs value) {
        return new JAXBElement<MessagePayloadTypeRs>(_PayloadRs_QNAME, MessagePayloadTypeRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "LocationPostalCode")
    public JAXBElement<String> createLocationPostalCode(String value) {
        return new JAXBElement<String>(_LocationPostalCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonDeathDate")
    public JAXBElement<XMLGregorianCalendar> createPersonDeathDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PersonDeathDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonFacialImage")
    public JAXBElement<ImageType> createPersonFacialImage(ImageType value) {
        return new JAXBElement<ImageType>(_PersonFacialImage_QNAME, ImageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaritalStatusCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonMaritalStatusCode")
    public JAXBElement<MaritalStatusCodeType> createPersonMaritalStatusCode(MaritalStatusCodeType value) {
        return new JAXBElement<MaritalStatusCodeType>(_PersonMaritalStatusCode_QNAME, MaritalStatusCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonSurname")
    public JAXBElement<String> createPersonSurname(String value) {
        return new JAXBElement<String>(_PersonSurname_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessagePropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageProperty")
    public JAXBElement<MessagePropertyType> createMessageProperty(MessagePropertyType value) {
        return new JAXBElement<MessagePropertyType>(_MessageProperty_QNAME, MessagePropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonMaidenName")
    public JAXBElement<String> createPersonMaidenName(String value) {
        return new JAXBElement<String>(_PersonMaidenName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessagePayloadTypeRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0", name = "Payload")
    public JAXBElement<MessagePayloadTypeRq> createPayload(MessagePayloadTypeRq value) {
        return new JAXBElement<MessagePayloadTypeRq>(_Payload_QNAME, MessagePayloadTypeRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "SANationalID")
    public JAXBElement<String> createSANationalID(String value) {
        return new JAXBElement<String>(_SANationalID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageNumber")
    public JAXBElement<String> createMessageNumber(String value) {
        return new JAXBElement<String>(_MessageNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonLivingIndicator")
    public JAXBElement<Boolean> createPersonLivingIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_PersonLivingIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonName")
    public JAXBElement<PersonNameType> createPersonName(PersonNameType value) {
        return new JAXBElement<PersonNameType>(_PersonName_QNAME, PersonNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "Timestamp")
    public JAXBElement<XMLGregorianCalendar> createTimestamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_Timestamp_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserMessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "UserMessage")
    public JAXBElement<UserMessageType> createUserMessage(UserMessageType value) {
        return new JAXBElement<UserMessageType>(_UserMessage_QNAME, UserMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SouthAfricanProvincesCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "LocationProvinceCode")
    public JAXBElement<SouthAfricanProvincesCodeType> createLocationProvinceCode(SouthAfricanProvincesCodeType value) {
        return new JAXBElement<SouthAfricanProvincesCodeType>(_LocationProvinceCode_QNAME, SouthAfricanProvincesCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonBirthDate")
    public JAXBElement<XMLGregorianCalendar> createPersonBirthDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PersonBirthDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageHeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "Header")
    public JAXBElement<MessageHeaderType> createHeader(MessageHeaderType value) {
        return new JAXBElement<MessageHeaderType>(_Header_QNAME, MessageHeaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DHATransactionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "DHA_Transaction")
    public JAXBElement<DHATransactionType> createDHATransaction(DHATransactionType value) {
        return new JAXBElement<DHATransactionType>(_DHATransaction_QNAME, DHATransactionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountriesCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "IdentityDocumentCountryCode")
    public JAXBElement<CountriesCodeType> createIdentityDocumentCountryCode(CountriesCodeType value) {
        return new JAXBElement<CountriesCodeType>(_IdentityDocumentCountryCode_QNAME, CountriesCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonAssignedIdentityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonIdentification")
    public JAXBElement<PersonAssignedIdentityType> createPersonIdentification(PersonAssignedIdentityType value) {
        return new JAXBElement<PersonAssignedIdentityType>(_PersonIdentification_QNAME, PersonAssignedIdentityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContactTypeCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "ContactTypeCode")
    public JAXBElement<ContactTypeCodeType> createContactTypeCode(ContactTypeCodeType value) {
        return new JAXBElement<ContactTypeCodeType>(_ContactTypeCode_QNAME, ContactTypeCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MaritalTypeCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonMaritalTypeCode")
    public JAXBElement<MaritalTypeCodeType> createPersonMaritalTypeCode(MaritalTypeCodeType value) {
        return new JAXBElement<MaritalTypeCodeType>(_PersonMaritalTypeCode_QNAME, MaritalTypeCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageId")
    public JAXBElement<MessageIdType> createMessageId(MessageIdType value) {
        return new JAXBElement<MessageIdType>(_MessageId_QNAME, MessageIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContactInformationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonContactInformation")
    public JAXBElement<ContactInformationType> createPersonContactInformation(ContactInformationType value) {
        return new JAXBElement<ContactInformationType>(_PersonContactInformation_QNAME, ContactInformationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonPostalAddress")
    public JAXBElement<AddressType> createPersonPostalAddress(AddressType value) {
        return new JAXBElement<AddressType>(_PersonPostalAddress_QNAME, AddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonMarriageDate")
    public JAXBElement<XMLGregorianCalendar> createPersonMarriageDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PersonMarriageDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "BinaryObject_Base64")
    public JAXBElement<byte[]> createBinaryObjectBase64(byte[] value) {
        return new JAXBElement<byte[]>(_BinaryObjectBase64_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageHeaderDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageData")
    public JAXBElement<MessageHeaderDataType> createMessageData(MessageHeaderDataType value) {
        return new JAXBElement<MessageHeaderDataType>(_MessageData_QNAME, MessageHeaderDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "ContactDetailsText")
    public JAXBElement<String> createContactDetailsText(String value) {
        return new JAXBElement<String>(_ContactDetailsText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "TransactionDateTime")
    public JAXBElement<XMLGregorianCalendar> createTransactionDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TransactionDateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "TransactionID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTransactionID(String value) {
        return new JAXBElement<String>(_TransactionID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "IdentityDocumentNumber")
    public JAXBElement<String> createIdentityDocumentNumber(String value) {
        return new JAXBElement<String>(_IdentityDocumentNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonGenderCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonGenderCode")
    public JAXBElement<PersonGenderCodeType> createPersonGenderCode(PersonGenderCodeType value) {
        return new JAXBElement<PersonGenderCodeType>(_PersonGenderCode_QNAME, PersonGenderCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonIdentityProfileType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonIdentityProfile")
    public JAXBElement<PersonIdentityProfileType> createPersonIdentityProfile(PersonIdentityProfileType value) {
        return new JAXBElement<PersonIdentityProfileType>(_PersonIdentityProfile_QNAME, PersonIdentityProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "PersonGivenName")
    public JAXBElement<String> createPersonGivenName(String value) {
        return new JAXBElement<String>(_PersonGivenName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageInfo")
    public JAXBElement<MessageInfoType> createMessageInfo(MessageInfoType value) {
        return new JAXBElement<MessageInfoType>(_MessageInfo_QNAME, MessageInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DHAPersonIdentityProfileType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "DHA_PersonIdentityProfile")
    public JAXBElement<DHAPersonIdentityProfileType> createDHAPersonIdentityProfile(DHAPersonIdentityProfileType value) {
        return new JAXBElement<DHAPersonIdentityProfileType>(_DHAPersonIdentityProfile_QNAME, DHAPersonIdentityProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IJSMessageSourceCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", name = "MessageSource")
    public JAXBElement<IJSMessageSourceCodeType> createMessageSource(IJSMessageSourceCodeType value) {
        return new JAXBElement<IJSMessageSourceCodeType>(_MessageSource_QNAME, IJSMessageSourceCodeType.class, null, value);
    }

}
