
package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * A structure that describes metadata common to most other structures.
 * 
 * <p>Java class for SuperType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SuperType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://ijs.gov.za/schemas/sajxdm/2.0.0}SuperTypeMetadata"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SuperType")
@XmlSeeAlso({
    PersonIdentityProfileType.class,
    AddressType.class,
    ContactInformationType.class,
    TransactionType.class,
    ErrorType.class,
    BinaryType.class
})
public class SuperType {


}
