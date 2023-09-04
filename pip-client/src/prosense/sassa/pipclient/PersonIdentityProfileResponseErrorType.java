package prosense.sassa.pipclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * This message will be sent from DHA in case an error is encountered. more than one instance of the ID number was found, an error message must be returned, using this message.
 * 
 * <p>Java class for PersonIdentityProfileResponseErrorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentityProfileResponseErrorType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajsip/3.0.0}ResponseErrorMessageType">
 *       &lt;sequence>
 *         &lt;element name="Error">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}DHA_Error" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentityProfileResponseErrorType", namespace = "http://ijs.gov.za/schemas/messages/PersonIdentityProfile/ResponseError/1.0", propOrder = {
    "error"
})
@XmlSeeAlso({
    PersonIdentityProfileResponseError.class
})
public class PersonIdentityProfileResponseErrorType
    extends ResponseErrorMessageType
{

    @XmlElement(name = "Error", required = true)
    protected PersonIdentityProfileResponseErrorType.Error error;

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link PersonIdentityProfileResponseErrorType.Error }
     *     
     */
    public PersonIdentityProfileResponseErrorType.Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonIdentityProfileResponseErrorType.Error }
     *     
     */
    public void setError(PersonIdentityProfileResponseErrorType.Error value) {
        this.error = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}DHA_Error" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dhaError"
    })
    public static class Error {

        @XmlElement(name = "DHA_Error", required = true)
        protected List<DHAErrorType> dhaError;

        /**
         * Gets the value of the dhaError property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dhaError property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDHAError().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DHAErrorType }
         * 
         * 
         */
        public List<DHAErrorType> getDHAError() {
            if (dhaError == null) {
                dhaError = new ArrayList<DHAErrorType>();
            }
            return this.dhaError;
        }

    }

}
