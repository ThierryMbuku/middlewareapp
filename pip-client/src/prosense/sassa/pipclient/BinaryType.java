package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * A structure that describes a digital representation of an object encoded in a binary format.
 * 
 * <p>Java class for BinaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SuperType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}BinaryObject_Base64"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryType", propOrder = {
    "binaryObjectBase64"
})
@XmlSeeAlso({
    ImageType.class
})
public class BinaryType
    extends SuperType
{

    @XmlElement(name = "BinaryObject_Base64", required = true)
    protected byte[] binaryObjectBase64;

    /**
     * A binary encoding of data, e.g., a binary encoding of a picture, photo, image, graphic, sound, or video.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaryObjectBase64() {
        return binaryObjectBase64;
    }

    /**
     * Sets the value of the binaryObjectBase64 property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaryObjectBase64(byte[] value) {
        this.binaryObjectBase64 = value;
    }

}
