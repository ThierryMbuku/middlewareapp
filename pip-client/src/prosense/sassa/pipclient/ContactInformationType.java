package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A structure that describes details about how to contact a person or an organization.
 * 
 * <p>Java class for ContactInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactInformationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SuperType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ContactTypeCode"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ContactDetailsText"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInformationType", propOrder = {
    "contactTypeCode",
    "contactDetailsText"
})
public class ContactInformationType
    extends SuperType
{

    @XmlElement(name = "ContactTypeCode", required = true)
    protected ContactTypeCodeType contactTypeCode;
    @XmlElement(name = "ContactDetailsText", required = true)
    protected String contactDetailsText;

    /**
     * The ICS code identifying the type of contact information.  For example, work telephone, cell phone, email, etc.
     * 
     * @return
     *     possible object is
     *     {@link ContactTypeCodeType }
     *     
     */
    public ContactTypeCodeType getContactTypeCode() {
        return contactTypeCode;
    }

    /**
     * Sets the value of the contactTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactTypeCodeType }
     *     
     */
    public void setContactTypeCode(ContactTypeCodeType value) {
        this.contactTypeCode = value;
    }

    /**
     * Details of the contact information.This could the contact telephone number or email address. etc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactDetailsText() {
        return contactDetailsText;
    }

    /**
     * Sets the value of the contactDetailsText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactDetailsText(String value) {
        this.contactDetailsText = value;
    }

}
