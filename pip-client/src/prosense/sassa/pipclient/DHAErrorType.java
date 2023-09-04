
package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Department of Home Affairs error.
 * 
 * <p>Java class for DHA_ErrorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DHA_ErrorType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ErrorType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ErrorCode"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DHA_ErrorType", propOrder = {
    "errorCode"
})
public class DHAErrorType
    extends ErrorType
{

    @XmlElement(name = "ErrorCode", required = true)
    protected ErrorCodeDHACodeType errorCode;

    /**
     * The ICS Code that uniquely identifies the error that has occurred.  
     * 
     * @return
     *     possible object is
     *     {@link ErrorCodeDHACodeType }
     *     
     */
    public ErrorCodeDHACodeType getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorCodeDHACodeType }
     *     
     */
    public void setErrorCode(ErrorCodeDHACodeType value) {
        this.errorCode = value;
    }

}
