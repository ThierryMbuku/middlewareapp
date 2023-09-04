
package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Base message type. The base message is composed of the header, payload and error content.
 * 
 * <p>Java class for MessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{http://ijs.gov.za/schemas/sajsip/3.0.0}typeId use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0")
@XmlSeeAlso({
    ResponseErrorMessageType.class,
    ResponseMessageType.class,
    RequestMessageType.class
})
public class MessageType {

    @XmlAttribute(name = "typeId", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", required = true)
    protected String typeId;

    /**
     * Six-digit identifier. Message type identifier. Is constant for all versions of the schema for this message. Values are pre-assigned by the IJS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeId(String value) {
        this.typeId = value;
    }

}
