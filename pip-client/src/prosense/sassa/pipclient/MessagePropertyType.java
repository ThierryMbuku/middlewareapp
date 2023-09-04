package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Message attribute property type.
 * 
 * <p>Java class for MessagePropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessagePropertyType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://ijs.gov.za/schemas/sajsip/3.0.0>string">
 *       &lt;attribute name="name" use="required" type="{http://ijs.gov.za/schemas/sajsip/3.0.0}MessagePropertyNameType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePropertyType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0", propOrder = {
    "value"
})
public class MessagePropertyType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Text string value. Derived from xsd:string.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
