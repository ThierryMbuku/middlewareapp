package prosense.sassa.oaamwrapper.app.control;

import java.util.Hashtable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.InjectionPoint;

import javax.jms.JMSException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.bharosa.vcrypt.common.util.VCryptObjectResponse;
import com.bharosa.vcrypt.common.util.VCryptCommonUtil;
import com.bharosa.vcrypt.tracker.impl.VCryptTrackerImpl;
import com.bharosa.vcrypt.common.data.OAAMUserData;
import com.bharosa.vcrypt.common.data.OAAMIPData;
import com.bharosa.vcrypt.common.data.OAAMSessionData;
import com.bharosa.vcrypt.common.data.OAAMDeviceFingerprintData;

import com.fasterxml.jackson.databind.ObjectMapper;

import prosense.sassa.oaamwrapper.api.control.Context;

@ApplicationScoped
public class App {
// jms
// 	private static final String JMS_URL = "t3://localhost:7003";
// 	private static final String JMS_URL = "t3://biamqanrpshc01.sassa.local:7003,biamqanrpshc02.sassa.local:7003,biamdrnrpshc02.sassa.local:7003";
	private static final String JMS_URL = "t3://biamprnrpphc01.sassa.local:7003,biamprnrpphc02.sassa.local:7003,biamdrnrpshc01.sassa.local:7003";
    private static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
    private static final String SECURITY_PRINCIPAL = "weblogic";
//     private static final String SECURITY_CREDENTIALS = "Oracle123";
//     private static final String SECURITY_CREDENTIALS = "weblogic123";
    private static final String SECURITY_CREDENTIALS = "welcome1";
// oaam
// 	private static final String LOGIN_NAME = "oaamtest1";
	private static final String LOGIN_NAME = "oaamadmin_nrp";
	private static final String GROUP_NAME = "Default";
	private static final String IP = "172.16.128.21";
	
	public static String sessionId = null;

    @Produces
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Produces
	@Context
    public InitialContext initialContext() throws NamingException, JMSException {
        Hashtable<String, String> env = new Hashtable();
		env.put(javax.naming.Context.PROVIDER_URL, JMS_URL);
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(javax.naming.Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
		env.put(javax.naming.Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
		return new InitialContext(env);
    }

    @PostConstruct
    public static void oaamSession() throws Exception {
    	System.out.println("===== oaamSession() =====");
    	String requestId = VCryptCommonUtil.generateUID(String.valueOf(System.currentTimeMillis()));
    	OAAMUserData user = new OAAMUserData(LOGIN_NAME, GROUP_NAME, LOGIN_NAME);
    	OAAMIPData ip = new OAAMIPData(IP, Double.valueOf("0"), Double.valueOf("0"));
    	OAAMSessionData sn = new OAAMSessionData(0);
    	OAAMDeviceFingerprintData prtData = new OAAMDeviceFingerprintData(1,"","");
    	List<OAAMDeviceFingerprintData> cookieList = new ArrayList<OAAMDeviceFingerprintData>();
    	cookieList.add(prtData);
    	VCryptTrackerImpl impl = new VCryptTrackerImpl();
    	System.out.println("===== 0 =====");
    	VCryptObjectResponse session = impl.createOAAMSession(requestId, new Date(), user, ip, cookieList, sn);
    	System.out.println("===== 1 =====");
    	System.out.println(session);
    	sessionId = requestId;
    	System.out.println("===== 2 =====");
    }
}
