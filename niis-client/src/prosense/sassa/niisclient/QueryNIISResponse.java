package prosense.sassa.niisclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="QueryNIISResult" type="{http://tempuri.org/}NIISResults" minOccurs="0"/>
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
    "queryNIISResult"
})
@XmlRootElement(name = "QueryNIISResponse")
public class QueryNIISResponse {

    @XmlElement(name = "QueryNIISResult")
    protected NIISResults queryNIISResult;

    /**
     * Gets the value of the queryNIISResult property.
     * 
     * @return
     *     possible object is
     *     {@link NIISResults }
     *     
     */
    public NIISResults getQueryNIISResult() {
        return queryNIISResult;
    }

    /**
     * Sets the value of the queryNIISResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link NIISResults }
     *     
     */
    public void setQueryNIISResult(NIISResults value) {
        this.queryNIISResult = value;
    }

}
