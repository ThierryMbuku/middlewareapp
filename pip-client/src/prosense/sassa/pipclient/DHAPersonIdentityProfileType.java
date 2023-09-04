package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Person Identity information as recorded by DHA, and referenced by the specific person's SA identity number, including DHA transaction information.
 * 
 * <p>Java class for DHA_PersonIdentityProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DHA_PersonIdentityProfileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}DHA_Transaction"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonIdentityProfile" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DHA_PersonIdentityProfileType", propOrder = {
    "dhaTransaction",
    "personIdentityProfile"
})
public class DHAPersonIdentityProfileType {

    @XmlElement(name = "DHA_Transaction", required = true)
    protected DHATransactionType dhaTransaction;
    @XmlElement(name = "PersonIdentityProfile", required = true)
    protected List<PersonIdentityProfileType> personIdentityProfile;

    /**
     * Gets the value of the dhaTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link DHATransactionType }
     *     
     */
    public DHATransactionType getDHATransaction() {
        return dhaTransaction;
    }

    /**
     * Sets the value of the dhaTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link DHATransactionType }
     *     
     */
    public void setDHATransaction(DHATransactionType value) {
        this.dhaTransaction = value;
    }

    /**
     * Gets the value of the personIdentityProfile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personIdentityProfile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonIdentityProfile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonIdentityProfileType }
     * 
     * 
     */
    public List<PersonIdentityProfileType> getPersonIdentityProfile() {
        if (personIdentityProfile == null) {
            personIdentityProfile = new ArrayList<PersonIdentityProfileType>();
        }
        return this.personIdentityProfile;
    }

}
