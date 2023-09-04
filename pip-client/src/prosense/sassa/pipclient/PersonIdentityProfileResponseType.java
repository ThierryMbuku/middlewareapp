package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * This message will be sent from DHA, and will be triggered and returned for every Person Identity Profile Request Message received, unless an error is encountered.
 * 
 * <p>Java class for PersonIdentityProfileResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentityProfileResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajsip/3.0.0}ResponseMessageType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Response/1.0}PayloadRs"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentityProfileResponseType", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Response/1.0", propOrder = {
    "payloadRs"
})
@XmlSeeAlso({
    PersonIdentityProfileResponse.class
})
public class PersonIdentityProfileResponseType
    extends ResponseMessageType
{

    @XmlElement(name = "Payload", required = true)
    protected MessagePayloadTypeRs payloadRs;

    /**
     * Response information containing the person's profile as recorded by the DHA.
     * 
     * @return
     *     possible object is
     *     {@link MessagePayloadTypeRs }
     *     
     */
    public MessagePayloadTypeRs getPayloadRs() {
        return payloadRs;
    }

    /**
     * Sets the value of the payloadRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePayloadTypeRs }
     *     
     */
    public void setPayloadRs(MessagePayloadTypeRs value) {
        this.payloadRs = value;
    }

}
