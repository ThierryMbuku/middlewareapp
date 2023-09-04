package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Identity information assigned to a person, including the unique document type, number and country of issue.
 * 
 * <p>Java class for PersonAssignedIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonAssignedIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}IdentityDocumentType"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}IdentityDocumentNumber"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}IdentityDocumentCountryCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonAssignedIdentityType", propOrder = {
    "identityDocumentType",
    "identityDocumentNumber",
    "identityDocumentCountryCode"
})
public class PersonAssignedIdentityType {

    @XmlElement(name = "IdentityDocumentType", required = true)
    protected IdentityDocumentTypeCodeType identityDocumentType;
    @XmlElement(name = "IdentityDocumentNumber", required = true)
    protected String identityDocumentNumber;
    @XmlElement(name = "IdentityDocumentCountryCode")
    protected CountriesCodeType identityDocumentCountryCode;

    /**
     * The ICS code for the identity document type.
     * 
     * @return
     *     possible object is
     *     {@link IdentityDocumentTypeCodeType }
     *     
     */
    public IdentityDocumentTypeCodeType getIdentityDocumentType() {
        return identityDocumentType;
    }

    /**
     * Sets the value of the identityDocumentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentityDocumentTypeCodeType }
     *     
     */
    public void setIdentityDocumentType(IdentityDocumentTypeCodeType value) {
        this.identityDocumentType = value;
    }

    /**
     * The identity number associated with the identity document type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocumentNumber() {
        return identityDocumentNumber;
    }

    /**
     * Sets the value of the identityDocumentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocumentNumber(String value) {
        this.identityDocumentNumber = value;
    }

    /**
     * The ICS code for the country of document issue.
     * 
     * @return
     *     possible object is
     *     {@link CountriesCodeType }
     *     
     */
    public CountriesCodeType getIdentityDocumentCountryCode() {
        return identityDocumentCountryCode;
    }

    /**
     * Sets the value of the identityDocumentCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountriesCodeType }
     *     
     */
    public void setIdentityDocumentCountryCode(CountriesCodeType value) {
        this.identityDocumentCountryCode = value;
    }

}
