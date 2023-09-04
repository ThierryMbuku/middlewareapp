
package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * IJS ICS reference data type.
 * 
 * <p>Java class for ReferenceDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferenceDataType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://ijs.gov.za/schemas/sajxdm/2.0.0>ReferenceDataValueType">
 *       &lt;attribute name="Type" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataTypeType" />
 *       &lt;attribute name="Name" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataNameType" />
 *       &lt;attribute name="Version" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataVersionType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceDataType", propOrder = {
    "value"
})
@XmlSeeAlso({
    PersonGenderCodeType.class,
    ContactTypeCodeType.class,
    MaritalTypeCodeType.class,
    CountriesCodeType.class,
    SouthAfricanProvincesCodeType.class,
    IdentityDocumentTypeCodeType.class,
    MaritalStatusCodeType.class,
    ErrorCodeDHACodeType.class
})
public class ReferenceDataType {

    @XmlValue
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String value;
    @XmlAttribute(name = "Type", required = true)
    protected ReferenceDataTypeType type;
    @XmlAttribute(name = "Name", required = true)
    protected String name;
    @XmlAttribute(name = "Version", required = true)
    protected String version;

    /**
     * IJS ICS reference data value or token.
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceDataTypeType }
     *     
     */
    public ReferenceDataTypeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceDataTypeType }
     *     
     */
    public void setType(ReferenceDataTypeType value) {
        this.type = value;
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

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
