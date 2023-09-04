package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReferenceDataTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReferenceDataTypeType">
 *   &lt;restriction base="{http://ijs.gov.za/schemas/sajxdm/2.0.0}string">
 *     &lt;enumeration value="ICS_refdata"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReferenceDataTypeType")
@XmlEnum
public enum ReferenceDataTypeType {

    @XmlEnumValue("ICS_refdata")
    ICS_REFDATA("ICS_refdata");
    private final String value;

    ReferenceDataTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReferenceDataTypeType fromValue(String v) {
        for (ReferenceDataTypeType c: ReferenceDataTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
