package prosense.sassa.niisclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfNIISDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfNIISDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NIISDetails" type="{http://tempuri.org/}NIISDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfNIISDetails", propOrder = {
    "niisDetails"
})
public class ArrayOfNIISDetails {

    @XmlElement(name = "NIISDetails", nillable = true)
    protected List<NIISDetails> niisDetails;

    /**
     * Gets the value of the niisDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the niisDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNIISDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NIISDetails }
     * 
     * 
     */
    public List<NIISDetails> getNIISDetails() {
        if (niisDetails == null) {
            niisDetails = new ArrayList<NIISDetails>();
        }
        return this.niisDetails;
    }

}
