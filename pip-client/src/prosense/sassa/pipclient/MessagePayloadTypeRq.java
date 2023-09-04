package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Person identity profile request payload.
 * 
 * <p>Java class for MessagePayloadType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessagePayloadType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessagePayloadType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SANationalID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePayloadTypeRq", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0")
public class MessagePayloadTypeRq
    extends MessagePayloadType3
{
    @XmlElement(namespace="http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "SANationalID", required = true)
    protected String saNationalID;


    public String getSANationalID() {
        return saNationalID;
    }

    public void setSANationalID(String saNationalID) {
        this.saNationalID = saNationalID;
    }
}
