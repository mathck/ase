//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.25 um 06:32:47 PM CET 
//


package at.tuwien.ase.model.javax;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.tuwien.ase.model.javax package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TaskBodyBr_QNAME = new QName("", "br");
    private final static QName _TaskBodyB_QNAME = new QName("", "b");
    private final static QName _TaskBodyH1_QNAME = new QName("", "h1");
    private final static QName _TaskBodyH2_QNAME = new QName("", "h2");
    private final static QName _TaskBodyH3_QNAME = new QName("", "h3");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.tuwien.ase.model.javax
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Template }
     * 
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link Identifier }
     * 
     */
    public Identifier createIdentifier() {
        return new Identifier();
    }

    /**
     * Create an instance of {@link TaskBody }
     * 
     */
    public TaskBody createTaskBody() {
        return new TaskBody();
    }

    /**
     * Create an instance of {@link TaskElements }
     * 
     */
    public TaskElements createTaskElements() {
        return new TaskElements();
    }

    /**
     * Create an instance of {@link TaskElement }
     * 
     */
    public TaskElement createTaskElement() {
        return new TaskElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "br", scope = TaskBody.class)
    public JAXBElement<String> createTaskBodyBr(String value) {
        return new JAXBElement<String>(_TaskBodyBr_QNAME, String.class, TaskBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "b", scope = TaskBody.class)
    public JAXBElement<String> createTaskBodyB(String value) {
        return new JAXBElement<String>(_TaskBodyB_QNAME, String.class, TaskBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "h1", scope = TaskBody.class)
    public JAXBElement<String> createTaskBodyH1(String value) {
        return new JAXBElement<String>(_TaskBodyH1_QNAME, String.class, TaskBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "h2", scope = TaskBody.class)
    public JAXBElement<String> createTaskBodyH2(String value) {
        return new JAXBElement<String>(_TaskBodyH2_QNAME, String.class, TaskBody.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "h3", scope = TaskBody.class)
    public JAXBElement<String> createTaskBodyH3(String value) {
        return new JAXBElement<String>(_TaskBodyH3_QNAME, String.class, TaskBody.class, value);
    }

}
