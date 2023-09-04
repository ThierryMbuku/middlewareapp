package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Redefined MessagInfo type to include message ID and reference-to-message ID. The RefToMessageId must contains the same ID as the MessageID of the original Request message.
 * 
 * <p>Java class for MessageInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}Timestamp"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageId"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageInfoType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "timestamp",
    "messageId"
})
public class MessageInfoType {

    @XmlElement(name = "Timestamp", required = true)
    protected XMLGregorianCalendar timestamp;
    @XmlElement(name = "MessageId", required = true)
    protected MessageIdType messageId;

    /**
     * Message instance date/time stamp. The date and time that the message was created by the source system.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Message instance  identification information. Includes message source and message number.
     * 
     * @return
     *     possible object is
     *     {@link MessageIdType }
     *     
     */
    public MessageIdType getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageIdType }
     *     
     */
    public void setMessageId(MessageIdType value) {
        this.messageId = value;
    }

}
