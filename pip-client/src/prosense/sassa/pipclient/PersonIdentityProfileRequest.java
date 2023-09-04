package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0}PersonIdentityProfileRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}Header"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0}Payload"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}typeId use="required" fixed="000063""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PersonIdentityProfileRequest", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/Request/1.0")
public class PersonIdentityProfileRequest
    extends PersonIdentityProfileRequestType
{


}
