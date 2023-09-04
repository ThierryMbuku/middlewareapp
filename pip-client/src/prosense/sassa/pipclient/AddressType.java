package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A structure that describes a postal location or a geographical location.
 * 
 * <p>Java class for AddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SuperType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}AddressLineText" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}LocationProvinceCode" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}LocationPostalCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "addressLineText",
    "locationProvinceCode",
    "locationPostalCode"
})
public class AddressType
    extends SuperType
{

    @XmlElement(name = "AddressLineText", required = true)
    protected List<String> addressLineText;
    @XmlElement(name = "LocationProvinceCode")
    protected SouthAfricanProvincesCodeType locationProvinceCode;
    @XmlElement(name = "LocationPostalCode")
    protected String locationPostalCode;

    /**
     * Details about the address.  This can include the building detail, room number, street name, street number, etc.Gets the value of the addressLineText property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressLineText property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressLineText().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddressLineText() {
        if (addressLineText == null) {
            addressLineText = new ArrayList<String>();
        }
        return this.addressLineText;
    }

    /**
     * The ICS code identifying the province of the address.
     * 
     * @return
     *     possible object is
     *     {@link SouthAfricanProvincesCodeType }
     *     
     */
    public SouthAfricanProvincesCodeType getLocationProvinceCode() {
        return locationProvinceCode;
    }

    /**
     * Sets the value of the locationProvinceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link SouthAfricanProvincesCodeType }
     *     
     */
    public void setLocationProvinceCode(SouthAfricanProvincesCodeType value) {
        this.locationProvinceCode = value;
    }

    /**
     * The numeric postal code of the address.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationPostalCode() {
        return locationPostalCode;
    }

    /**
     * Sets the value of the locationPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationPostalCode(String value) {
        this.locationPostalCode = value;
    }

}
