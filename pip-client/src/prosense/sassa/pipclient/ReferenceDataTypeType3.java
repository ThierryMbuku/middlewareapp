
package prosense.sassa.pipclient;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReferenceDataTypeType3.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReferenceDataTypeType3">
 *   &lt;restriction base="{http://ijs.gov.za/schemas/sajsip/3.0.0}string">
 *     &lt;enumeration value="ICS_refdata"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReferenceDataTypeType3", namespace = "http://ijs.gov.za/schemas/sajsip/3.0.0")
@XmlEnum
public enum ReferenceDataTypeType3 {

    @XmlEnumValue("ICS_refdata")
    ICS_REFDATA("ICS_refdata");
    private final String value;

    ReferenceDataTypeType3(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReferenceDataTypeType3 fromValue(String v) {
        for (ReferenceDataTypeType3 c: ReferenceDataTypeType3 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
