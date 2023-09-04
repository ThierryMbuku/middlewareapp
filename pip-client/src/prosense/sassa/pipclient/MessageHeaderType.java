package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Message instance application-specific data. Required to be sub-classed.
 * 
 * <p>Java class for MessageHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}UserMessage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageHeaderType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "userMessage"
})
public class MessageHeaderType {

    @XmlElement(name = "UserMessage", required = true)
    protected UserMessageType userMessage;

    /**
     * User message information. Users and application are responsible for providing the content for this element.
     * 
     * @return
     *     possible object is
     *     {@link UserMessageType }
     *     
     */
    public UserMessageType getUserMessage() {
        return userMessage;
    }

    /**
     * Sets the value of the userMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserMessageType }
     *     
     */
    public void setUserMessage(UserMessageType value) {
        this.userMessage = value;
    }

}
