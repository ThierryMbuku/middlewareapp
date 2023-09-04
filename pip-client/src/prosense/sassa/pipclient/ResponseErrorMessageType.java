package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Sub-classed MessageType. Subsitutes MessageInfo, removes the Payload element, and forces the Error element. The ResponseError message is sent to a the originator of a corresponding Request message.
 * 
 * <p>Java class for ResponseErrorMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseErrorMessageType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessageType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}Header"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseErrorMessageType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "header"
})
@XmlSeeAlso({
    PersonIdentityProfileResponseErrorType.class
})
public class ResponseErrorMessageType
    extends MessageType
{

    @XmlElement(name = "Header", required = true)
    protected MessageHeaderType header;

    /**
     * Message header. Always the first element of any SAJSIP message.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeaderType }
     *     
     */
    public MessageHeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeaderType }
     *     
     */
    public void setHeader(MessageHeaderType value) {
        this.header = value;
    }

}
