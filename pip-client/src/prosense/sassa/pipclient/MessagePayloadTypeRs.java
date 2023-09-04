package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Response information containing the person's profile as recorded by the DHA.
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
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}DHA_PersonIdentityProfile"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePayloadTypeRs", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Response/1.0")
public class MessagePayloadTypeRs
    extends MessagePayloadType3
{
    @XmlElement(namespace="http://ijs.gov.za/schemas/sajxdm/2.0.0", name = "DHA_PersonIdentityProfile", required = true)
    protected DHAPersonIdentityProfileType dha_PersonIdentityProfile;


    public DHAPersonIdentityProfileType getDHA_PersonIdentityProfile() {
        return dha_PersonIdentityProfile;
    }

    public void setDHA_PersonIdentityProfile(DHAPersonIdentityProfileType dha_PersonIdentityProfile) {
        this.dha_PersonIdentityProfile = dha_PersonIdentityProfile;
    }
}
