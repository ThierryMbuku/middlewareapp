package prosense.sassa.niisclient;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "NIISBankService", targetNamespace = "http://tempuri.org/", wsdlLocation = "file:src/prosense/sassa/niisclient/NIISBankService.wsdl")
public class NIISBankService
    extends Service
{

    private final static URL NIISBANKSERVICE_WSDL_LOCATION;
    private final static WebServiceException NIISBANKSERVICE_EXCEPTION;
    private final static QName NIISBANKSERVICE_QNAME = new QName("http://tempuri.org/", "NIISBankService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:src/prosense/sassa/niisclient/NIISBankService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        NIISBANKSERVICE_WSDL_LOCATION = url;
        NIISBANKSERVICE_EXCEPTION = e;
    }

    public NIISBankService() {
        super(__getWsdlLocation(), NIISBANKSERVICE_QNAME);
    }

    public NIISBankService(WebServiceFeature... features) {
        super(__getWsdlLocation(), NIISBANKSERVICE_QNAME, features);
    }

    public NIISBankService(URL wsdlLocation) {
        super(wsdlLocation, NIISBANKSERVICE_QNAME);
    }

    public NIISBankService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, NIISBANKSERVICE_QNAME, features);
    }

    public NIISBankService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NIISBankService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns NIISBankServiceSoap
     */
    @WebEndpoint(name = "NIISBankServiceSoap")
    public NIISBankServiceSoap getNIISBankServiceSoap() {
        return super.getPort(new QName("http://tempuri.org/", "NIISBankServiceSoap"), NIISBankServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NIISBankServiceSoap
     */
    @WebEndpoint(name = "NIISBankServiceSoap")
    public NIISBankServiceSoap getNIISBankServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "NIISBankServiceSoap"), NIISBankServiceSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns NIISBankServiceSoap
     */
    @WebEndpoint(name = "NIISBankServiceSoap12")
    public NIISBankServiceSoap getNIISBankServiceSoap12() {
        return super.getPort(new QName("http://tempuri.org/", "NIISBankServiceSoap12"), NIISBankServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NIISBankServiceSoap
     */
    @WebEndpoint(name = "NIISBankServiceSoap12")
    public NIISBankServiceSoap getNIISBankServiceSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "NIISBankServiceSoap12"), NIISBankServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (NIISBANKSERVICE_EXCEPTION!= null) {
            throw NIISBANKSERVICE_EXCEPTION;
        }
        return NIISBANKSERVICE_WSDL_LOCATION;
    }

}