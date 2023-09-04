package prosense.sassa.niisclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "NIISBankServiceSoap", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface NIISBankServiceSoap {


    /**
     * 
     * @param fileNo
     * @param idNo
     * @return
     *     returns prosense.sassa.niisclient.NIISResults
     */
    @WebMethod(operationName = "QueryNIIS", action = "http://tempuri.org/QueryNIIS")
    @WebResult(name = "QueryNIISResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "QueryNIIS", targetNamespace = "http://tempuri.org/", className = "prosense.sassa.niisclient.QueryNIIS")
    @ResponseWrapper(localName = "QueryNIISResponse", targetNamespace = "http://tempuri.org/", className = "prosense.sassa.niisclient.QueryNIISResponse")
    public NIISResults queryNIIS(
        @WebParam(name = "FileNo", targetNamespace = "http://tempuri.org/")
        String fileNo,
        @WebParam(name = "IDNo", targetNamespace = "http://tempuri.org/")
        String idNo);

}