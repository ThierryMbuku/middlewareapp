package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Application message information. Users and application are responsible for providing the content for this element.
 * 
 * <p>Java class for UserMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserMessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageInfo"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageProperty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserMessageType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "messageInfo",
    "messageProperty",
    "messageData"
})
public class UserMessageType {

    @XmlElement(name = "MessageInfo", required = true)
    protected MessageInfoType messageInfo;
    @XmlElement(name = "MessageProperty")
    protected List<MessagePropertyType> messageProperty;
    @XmlElement(name = "MessageData")
    protected MessageHeaderDataType messageData;

    /**
     * Message base source and instance identifiers for forward and return message definitions. Required to be substituted by each different type of message.
     * 
     * @return
     *     possible object is
     *     {@link MessageInfoType }
     *     
     */
    public MessageInfoType getMessageInfo() {
        return messageInfo;
    }

    /**
     * Sets the value of the messageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageInfoType }
     *     
     */
    public void setMessageInfo(MessageInfoType value) {
        this.messageInfo = value;
    }

    /**
     * Message instance attribute property.Gets the value of the messageProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessagePropertyType }
     * 
     * 
     */
    public List<MessagePropertyType> getMessageProperty() {
        if (messageProperty == null) {
            messageProperty = new ArrayList<MessagePropertyType>();
        }
        return this.messageProperty;
    }

    /**
     * Message instance application-specific data.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeaderDataType }
     *     
     */
    public MessageHeaderDataType getMessageData() {
        return messageData;
    }

    /**
     * Sets the value of the messageData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeaderDataType }
     *     
     */
    public void setMessageData(MessageHeaderDataType value) {
        this.messageData = value;
    }

}
