package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * ICS codeset type created for element 'ContactTypeCode'.
 * 
 * <p>Java class for ContactTypeCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactTypeCodeType">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://ijs.gov.za/schemas/sajxdm/2.0.0>ReferenceDataType">
 *       &lt;attribute name="Type" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataTypeType" />
 *       &lt;attribute name="Name" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataNameType" fixed="ContactType" />
 *       &lt;attribute name="Version" use="required" type="{http://ijs.gov.za/schemas/sajxdm/2.0.0}ReferenceDataVersionType" />
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactTypeCodeType")
public class ContactTypeCodeType
    extends ReferenceDataType
{


}
