//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.25 um 06:32:47 PM CET 
//


package at.tuwien.ase.model.javax;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für taskElementsType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="taskElementsType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="image"/>
 *     &lt;enumeration value="checkbox"/>
 *     &lt;enumeration value="textbox"/>
 *     &lt;enumeration value="file"/>
 *     &lt;enumeration value="slider"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "taskElementsType")
@XmlEnum
public enum TaskElementsType {

    @XmlEnumValue("image")
    IMAGE("image"),
    @XmlEnumValue("checkbox")
    CHECKBOX("checkbox"),
    @XmlEnumValue("textbox")
    TEXTBOX("textbox"),
    @XmlEnumValue("file")
    FILE("file"),
    @XmlEnumValue("slider")
    SLIDER("slider");
    private final String value;

    TaskElementsType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaskElementsType fromValue(String v) {
        for (TaskElementsType c: TaskElementsType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
