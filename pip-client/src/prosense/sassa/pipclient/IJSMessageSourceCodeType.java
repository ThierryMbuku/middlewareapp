package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * ICS codeset type created for element 'MessageSource'.
 * 
 * <p>Java class for IJSMessageSourceCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IJSMessageSourceCodeType">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://ijs.gov.za/schemas/sajsip/3.0.0>ReferenceDataType3">
 *       &lt;attribute name="Type" use="required" type="{http://ijs.gov.za/schemas/sajsip/3.0.0}ReferenceDataTypeType3" />
 *       &lt;attribute name="Name" use="required" type="{http://ijs.gov.za/schemas/sajsip/3.0.0}ReferenceDataNameType" fixed="IJSMessageSource" />
 *       &lt;attribute name="Version" use="required" type="{http://ijs.gov.za/schemas/sajsip/3.0.0}ReferenceDataVersionType" />
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IJSMessageSourceCodeType", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0")
public class IJSMessageSourceCodeType
    extends ReferenceDataType3
{


}
