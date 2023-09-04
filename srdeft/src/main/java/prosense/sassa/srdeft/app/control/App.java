package prosense.sassa.srdeft.app.control;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import java.io.FileInputStream;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.ObjectName;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.SerializedSystemIni;

import prosense.sassa.srdeft.api.control.Property;


@ApplicationScoped
public class App {
    @Inject
    @Property
    private String admin_host;

    @Inject
    @Property
    private String admin_port;

    @Inject
    @Property
    private String srd_server;

    private Properties environment;
    private Properties boot;

    public App() {
        environment = new Properties();
        boot = new Properties();
        try {
            environment.load(RestApp.class.getResourceAsStream("environment.properties"));
            boot.load(new FileInputStream(System.getenv("DOMAIN_HOME") + "/servers/AdminServer/security/boot.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Produces
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @PersistenceContext(unitName = "srdeftUserPu")
    private EntityManager em;

    @Produces
    @PersistenceContext(unitName = "srdUserPu")
    @Property
    private EntityManager emSRD;

    @Produces
    @Property
    public String property(final InjectionPoint ip) {
        final Property annotation = ip.getAnnotated().getAnnotation(Property.class);
        if (annotation.value().isEmpty()) {
            return environment.getProperty(ip.getMember().getName());
        }
        return environment.getProperty(annotation.value());
    }

    @Produces
    public Map<String, Object> mbeanServerDetails() {
		Map<String, Object> mbeanServerDetails =  new HashMap<>();
		try {
			JMXServiceURL serviceURL = new JMXServiceURL("t3", admin_host, Integer.valueOf(admin_port), "/jndi/weblogic.management.mbeanservers.domainruntime");
			Hashtable hashtable = new Hashtable();
			hashtable.put(Context.SECURITY_PRINCIPAL, new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(System.getenv("DOMAIN_HOME"))).decrypt(boot.getProperty("username")));
			hashtable.put(Context.SECURITY_CREDENTIALS, new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(System.getenv("DOMAIN_HOME"))).decrypt(boot.getProperty("password")));
			hashtable.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
			hashtable.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
			MBeanServerConnection connection = JMXConnectorFactory.connect(serviceURL, hashtable).getMBeanServerConnection();
			ObjectName service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
			ObjectName domain = (ObjectName)connection.getAttribute(service, "DomainConfiguration");
			ObjectName security = (ObjectName)connection.getAttribute(domain, "SecurityConfiguration");
			ObjectName realm = (ObjectName)connection.getAttribute(security, "DefaultRealm");
			ObjectName[] authenticationProviders = (ObjectName[])connection.getAttribute(realm, "AuthenticationProviders");
			mbeanServerDetails.put("connection", connection);
			for (int p = 0; authenticationProviders != null && p < authenticationProviders.length; p++) {
				String name = (String)connection.getAttribute(authenticationProviders[p], "Name");
				if (name.equals("DefaultAuthenticator")) {
					mbeanServerDetails.put("authenticator", authenticationProviders[p]);
					break;
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
		return mbeanServerDetails;
    }

    @Produces
    public DataSource srdDataSource() {
		DataSource srdDataSource = null;
		try {
			Hashtable hashtable = new Hashtable();
			hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			hashtable.put(Context.PROVIDER_URL, "t3://" + srd_server);
			Context ctx = new InitialContext(hashtable);
			srdDataSource = (javax.sql.DataSource) ctx.lookup("srd_user");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return srdDataSource;
    }
}
