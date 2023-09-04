package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A name by which a person is known.
 * 
 * <p>Java class for PersonNameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonNameType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonSurname"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonGivenName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}PersonMaidenName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonNameType", propOrder = {
    "personSurname",
    "personGivenName",
    "personMaidenName"
})
public class PersonNameType {

    @XmlElement(name = "PersonSurname", required = true)
    protected String personSurname;
    @XmlElement(name = "PersonGivenName")
    protected List<String> personGivenName;
    @XmlElement(name = "PersonMaidenName")
    protected String personMaidenName;

    /**
     * Person surname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonSurname() {
        return personSurname;
    }

    /**
     * Sets the value of the personSurname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonSurname(String value) {
        this.personSurname = value;
    }

    /**
     * Person first or given name.Gets the value of the personGivenName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personGivenName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonGivenName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPersonGivenName() {
        if (personGivenName == null) {
            personGivenName = new ArrayList<String>();
        }
        return this.personGivenName;
    }

    /**
     * The original surname of the person before changed by marriage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonMaidenName() {
        return personMaidenName;
    }

    /**
     * Sets the value of the personMaidenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonMaidenName(String value) {
        this.personMaidenName = value;
    }

}
