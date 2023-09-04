package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Person Identity information as recorded by DHA, and referenced by the specific person's SA identity number.
 * 
 * <p>Java class for PersonIdentityProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentityProfileType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SuperType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonName"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonFacialImage" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonContactInformation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonBirthDate"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonBirthCountryCode" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonLivingIndicator"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonDeathDate" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonGenderCode"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonMaritalStatusCode"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonMaritalTypeCode" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonMarriageDate" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonIdentification" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonResidentialAddress" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonPostalAddress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentityProfileType", propOrder = {
    "personName",
    "personFacialImage",
    "personContactInformation",
    "personBirthDate",
    "personBirthCountryCode",
    "personLivingIndicator",
    "personDeathDate",
    "personGenderCode",
    "personMaritalStatusCode",
    "personMaritalTypeCode",
    "personMarriageDate",
    "personIdentification",
    "personResidentialAddress",
    "personPostalAddress"
})
public class PersonIdentityProfileType
    extends SuperType
{

    @XmlElement(name = "PersonName", required = true)
    protected PersonNameType personName;
    @XmlElement(name = "PersonFacialImage")
    protected ImageType personFacialImage;
    @XmlElement(name = "PersonContactInformation")
    protected List<ContactInformationType> personContactInformation;
    @XmlElement(name = "PersonBirthDate", required = true)
    protected XMLGregorianCalendar personBirthDate;
    @XmlElement(name = "PersonBirthCountryCode")
    protected CountriesCodeType personBirthCountryCode;
    @XmlElement(name = "PersonLivingIndicator")
    protected boolean personLivingIndicator;
    @XmlElement(name = "PersonDeathDate")
    protected XMLGregorianCalendar personDeathDate;
    @XmlElement(name = "PersonGenderCode", required = true)
    protected PersonGenderCodeType personGenderCode;
    @XmlElement(name = "PersonMaritalStatusCode", required = true)
    protected MaritalStatusCodeType personMaritalStatusCode;
    @XmlElement(name = "PersonMaritalTypeCode")
    protected MaritalTypeCodeType personMaritalTypeCode;
    @XmlElement(name = "PersonMarriageDate")
    protected XMLGregorianCalendar personMarriageDate;
    @XmlElement(name = "PersonIdentification")
    protected List<PersonAssignedIdentityType> personIdentification;
    @XmlElement(name = "PersonResidentialAddress")
    protected AddressType personResidentialAddress;
    @XmlElement(name = "PersonPostalAddress")
    protected AddressType personPostalAddress;

    /**
     * The name of the specific person.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameType }
     *     
     */
    public PersonNameType getPersonName() {
        return personName;
    }

    /**
     * Sets the value of the personName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameType }
     *     
     */
    public void setPersonName(PersonNameType value) {
        this.personName = value;
    }

    /**
     * The digital image of the specifc person's face, as recorded by DHA. The image format is JPEG2000.
     * 
     * @return
     *     possible object is
     *     {@link ImageType }
     *     
     */
    public ImageType getPersonFacialImage() {
        return personFacialImage;
    }

    /**
     * Sets the value of the personFacialImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageType }
     *     
     */
    public void setPersonFacialImage(ImageType value) {
        this.personFacialImage = value;
    }

    /**
     * Contact details of the specific person.Gets the value of the personContactInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personContactInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonContactInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactInformationType }
     * 
     * 
     */
    public List<ContactInformationType> getPersonContactInformation() {
        if (personContactInformation == null) {
            personContactInformation = new ArrayList<ContactInformationType>();
        }
        return this.personContactInformation;
    }

    /**
     * Person birth date.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonBirthDate() {
        return personBirthDate;
    }

    /**
     * Sets the value of the personBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonBirthDate(XMLGregorianCalendar value) {
        this.personBirthDate = value;
    }

    /**
     * The ICS code identifying the country where the person was born.
     * 
     * @return
     *     possible object is
     *     {@link CountriesCodeType }
     *     
     */
    public CountriesCodeType getPersonBirthCountryCode() {
        return personBirthCountryCode;
    }

    /**
     * Sets the value of the personBirthCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountriesCodeType }
     *     
     */
    public void setPersonBirthCountryCode(CountriesCodeType value) {
        this.personBirthCountryCode = value;
    }

    /**
     * An indicator of whether the person is alive or dead.  The indicator will be true if the person is alive and false if the person is dead.
     * 
     */
    public boolean isPersonLivingIndicator() {
        return personLivingIndicator;
    }

    /**
     * Sets the value of the personLivingIndicator property.
     * 
     */
    public void setPersonLivingIndicator(boolean value) {
        this.personLivingIndicator = value;
    }

    /**
     * The date on which the person passed away.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonDeathDate() {
        return personDeathDate;
    }

    /**
     * Sets the value of the personDeathDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonDeathDate(XMLGregorianCalendar value) {
        this.personDeathDate = value;
    }

    /**
     * The ICS code identifying the gender of the person.
     * 
     * @return
     *     possible object is
     *     {@link PersonGenderCodeType }
     *     
     */
    public PersonGenderCodeType getPersonGenderCode() {
        return personGenderCode;
    }

    /**
     * Sets the value of the personGenderCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonGenderCodeType }
     *     
     */
    public void setPersonGenderCode(PersonGenderCodeType value) {
        this.personGenderCode = value;
    }

    /**
     * The ICS code that indicates the marital status of the person.
     * 
     * @return
     *     possible object is
     *     {@link MaritalStatusCodeType }
     *     
     */
    public MaritalStatusCodeType getPersonMaritalStatusCode() {
        return personMaritalStatusCode;
    }

    /**
     * Sets the value of the personMaritalStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaritalStatusCodeType }
     *     
     */
    public void setPersonMaritalStatusCode(MaritalStatusCodeType value) {
        this.personMaritalStatusCode = value;
    }

    /**
     * The ICS code identifying the type of marriage, if the specific person is married.
     * 
     * @return
     *     possible object is
     *     {@link MaritalTypeCodeType }
     *     
     */
    public MaritalTypeCodeType getPersonMaritalTypeCode() {
        return personMaritalTypeCode;
    }

    /**
     * Sets the value of the personMaritalTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaritalTypeCodeType }
     *     
     */
    public void setPersonMaritalTypeCode(MaritalTypeCodeType value) {
        this.personMaritalTypeCode = value;
    }

    /**
     * The date on which the person was married.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonMarriageDate() {
        return personMarriageDate;
    }

    /**
     * Sets the value of the personMarriageDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonMarriageDate(XMLGregorianCalendar value) {
        this.personMarriageDate = value;
    }

    /**
     * Identity information assigned to a person, including the unique document type, number and country of issue.Gets the value of the personIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonAssignedIdentityType }
     * 
     * 
     */
    public List<PersonAssignedIdentityType> getPersonIdentification() {
        if (personIdentification == null) {
            personIdentification = new ArrayList<PersonAssignedIdentityType>();
        }
        return this.personIdentification;
    }

    /**
     * The last recorded residential address of the person.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getPersonResidentialAddress() {
        return personResidentialAddress;
    }

    /**
     * Sets the value of the personResidentialAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setPersonResidentialAddress(AddressType value) {
        this.personResidentialAddress = value;
    }

    /**
     * The last recorded postal address of the person.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getPersonPostalAddress() {
        return personPostalAddress;
    }

    /**
     * Sets the value of the personPostalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setPersonPostalAddress(AddressType value) {
        this.personPostalAddress = value;
    }

}
