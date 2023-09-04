package prosense.sassa.oam.custom;

import java.util.Set;

import oracle.security.am.common.utilities.principal.OAMUserPrincipal;

import javax.security.auth.Subject;
import oracle.security.am.plugin.PluginAttributeContextType;
import oracle.security.am.plugin.PluginResponse;
import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.PluginConfig;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.authn.CredentialParam;
import oracle.security.am.plugin.impl.CredentialMetaData;
import oracle.security.am.plugin.impl.UserAction;
import oracle.security.am.plugin.impl.UserActionContext;
import oracle.security.am.plugin.impl.UserActionMetaData;
import oracle.security.am.plugin.impl.UserContextData;
import oracle.security.am.plugin.authn.PluginConstants;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class OAMCustomPlugin extends AbstractAuthenticationPlugIn {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public String getDescription() {
        return "Plugin Description";
    }

    @Override
    public Map<String, MonitoringData> getMonitoringData() {
        return Collections.emptyMap();
    }

    @Override
    public boolean getMonitoringStatus() {
        return false;
    }

    @Override
    public String getPluginName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getRevision() {
        return 0;
    }

    @Override
    public ExecutionStatus initialize(PluginConfig config) {
        super.initialize(config);

        ExecutionStatus status = ExecutionStatus.FAILURE;
        status = ExecutionStatus.SUCCESS;
        return status;
    }

    @Override
	public ExecutionStatus process(AuthenticationContext context) throws AuthenticationException {
		ExecutionStatus status = ExecutionStatus.SUCCESS;
		String credentialName = "match";
// 		String loginPageURL = "http://iammukesh.prosense.co.za:14100/sassa-oam-custom-login/scan.jsp";
		String loginPageURL = "http://biamdvohsshc01.sassa.local:7777/sassa-oam-custom-login/scan.jsp";
		CredentialParam param = context.getCredential().getParam(credentialName);
		System.out.println("process method called");
		if (param == null) {
			System.out.println("scan fingerprint page loaded");
			UserContextData matchContext = new UserContextData(credentialName, "Match Fingerprint", new CredentialMetaData(PluginConstants.PASSWORD));
			UserContextData urlContext = new UserContextData(loginPageURL, new CredentialMetaData("URL"));
			UserActionContext actionContext = new UserActionContext();
			actionContext.getContextData().add(matchContext);
			actionContext.getContextData().add(urlContext);
			UserActionMetaData userAction = UserActionMetaData.REDIRECT_GET;
			UserAction action = new UserAction(actionContext, userAction);
			context.setAction(action);
			status = ExecutionStatus.PAUSE;
		} else {
			System.out.println("param value:: " + param.getValue().toString());
			if (param.getValue().toString().equals("pass")) {
				status = ExecutionStatus.SUCCESS;
				this.updatePluginResponse(context);
			} else
				status = ExecutionStatus.FAILURE;
		 }
		return status;
	}

    @Override
    public void setMonitoringStatus(boolean status) {
    }

	private void updatePluginResponse(final AuthenticationContext context) {
		String retAttrs[] = (String[]) null;
		String dsRef = null;
		
        String userName = getUserName(context);
        Subject subject = new Subject();
    	subject.getPrincipals().add(new OAMUserPrincipal(userName));
        context.setSubject(subject);

		PluginResponse rsp = new PluginResponse();
		rsp.setName(PluginConstants.KEY_RETURN_ATTRIBUTE);
		rsp.setType(PluginAttributeContextType.LITERAL);
		rsp.setValue(retAttrs);
		context.addResponse(rsp);

		rsp = new PluginResponse();
		rsp.setName(PluginConstants.KEY_IDENTITY_STORE_REF);
		rsp.setType(PluginAttributeContextType.LITERAL);
		rsp.setValue(dsRef);
		context.addResponse(rsp);

        rsp = new PluginResponse();
        rsp.setName(PluginConstants.KEY_AUTHENTICATED_USER_NAME);
        rsp.setType(PluginAttributeContextType.LITERAL);
        Set<OAMUserPrincipal> userNamePrincipal = context.getSubject().getPrincipals(OAMUserPrincipal.class);
        rsp.setValue(userNamePrincipal.iterator().next().getName());
        context.addResponse(rsp);
    }
    
    private String getUserName(final AuthenticationContext context) {
        String userName = null;
        CredentialParam param = context.getCredential().getParam("KEY_USERNAME");
        if (param != null) {
            userName = (String) param.getValue();
        }
        if ((userName == null) || (userName.length() == 0)) {
            userName = context.getStringAttribute("KEY_USERNAME");
        }
        return userName;
    }
}