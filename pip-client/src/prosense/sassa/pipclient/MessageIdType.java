package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Globally uniquely defines a message instance in the IJS. the combination of the message number and message source Name, Version and item value, is guaranteed to be unique.
 * 
 * <p>Java class for MessageIdType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageIdType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageNumber"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageSource"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageIdType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "messageNumber",
    "messageSource"
})
public class MessageIdType {

    @XmlElement(name = "MessageNumber", required = true)
    protected String messageNumber;
    @XmlElement(name = "MessageSource", required = true)
    protected IJSMessageSourceCodeType messageSource;

    /**
     * Message number. Unique number for all instances of this message type, for a specific message source.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageNumber() {
        return messageNumber;
    }

    /**
     * Sets the value of the messageNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageNumber(String value) {
        this.messageNumber = value;
    }

    /**
     * Message source. Uniquely defines the application/departmental end-point that sources the message to the IJS.
     * 
     * @return
     *     possible object is
     *     {@link IJSMessageSourceCodeType }
     *     
     */
    public IJSMessageSourceCodeType getMessageSource() {
        return messageSource;
    }

    /**
     * Sets the value of the messageSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link IJSMessageSourceCodeType }
     *     
     */
    public void setMessageSource(IJSMessageSourceCodeType value) {
        this.messageSource = value;
    }

}
