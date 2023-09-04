package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Message sent from the requesting department/party through the IJS Interoperability Hub to DHA. Message will be sent whenever a person’s national identity number is known and the person’s identity profile is required.
 * 
 * <p>Java class for PersonIdentityProfileRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentityProfileRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajsip/3.0.0}RequestMessageType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0}Payload"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentityProfileRequestType", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0", propOrder = {
    "payload"
})
@XmlSeeAlso({
    PersonIdentityProfileRequest.class
})
public class PersonIdentityProfileRequestType
    extends RequestMessageType
{

    @XmlElement(name = "Payload", required = true)
    protected MessagePayloadTypeRq payload;

    /**
     * Person identity profile request payload.
     * 
     * @return
     *     possible object is
     *     {@link MessagePayloadTypeRq }
     *     
     */
    public MessagePayloadTypeRq getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePayloadTypeRq }
     *     
     */
    public void setPayload(MessagePayloadTypeRq value) {
        this.payload = value;
    }

}
