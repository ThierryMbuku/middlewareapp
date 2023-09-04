package prosense.sassa.niisclient;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the prosense.sassa.niisclient package.
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: prosense.sassa.niisclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryNIIS }
     * 
     */
    public QueryNIIS createQueryNIIS() {
        return new QueryNIIS();
    }

    /**
     * Create an instance of {@link QueryNIISResponse }
     * 
     */
    public QueryNIISResponse createQueryNIISResponse() {
        return new QueryNIISResponse();
    }

    /**
     * Create an instance of {@link NIISResults }
     * 
     */
    public NIISResults createNIISResults() {
        return new NIISResults();
    }

    /**
     * Create an instance of {@link ArrayOfNIISDetails }
     * 
     */
    public ArrayOfNIISDetails createArrayOfNIISDetails() {
        return new ArrayOfNIISDetails();
    }

    /**
     * Create an instance of {@link NIISDetails }
     * 
     */
    public NIISDetails createNIISDetails() {
        return new NIISDetails();
    }

}
