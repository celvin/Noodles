package me.chadthompson.adf.secureapp.auth;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.logging.ADFLogger;

import weblogic.servlet.security.ServletAuthentication;
import weblogic.security.URLCallbackHandler;
import weblogic.security.services.Authentication;

/**
 * This class contains methods required for the secure
 * application to authenticate against the container.
 * This class and the methods herein are specific to the
 * WebLogic Application Server.
 * @author:  Chad Thompson
 * June 06, 2012
 */
public class LoginAuthBean {
    
    private static ADFLogger _logger = 
        ADFLogger.createADFLogger(LoginAuthBean.class);
    
    private String _username;
    private String _password;
    

    public String performLogin(){
        
        _logger.info("Login Requested For User: " + _username);
        
        String un = _username;
        byte[] pw = _password.getBytes();
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)ctx.getExternalContext().getRequest();
        CallbackHandler handler = new URLCallbackHandler(un, pw);
        try {
           Subject mySubject = Authentication.login(handler);
        
           ServletAuthentication.runAs(mySubject, request);
           ServletAuthentication.generateNewSessionID(request);
           String loginUrl = "/adfAuthentication?success_url=/faces" + 
           ctx.getViewRoot().getViewId();
        HttpServletResponse response = 
             (HttpServletResponse)ctx.getExternalContext().getResponse();
        sendForward(request, response, loginUrl);
        } catch (FailedLoginException fle) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                               "Incorrect Username or Password",
                                               "An incorrect Username or Password" +
                                               " was specified");
             ctx.addMessage(null, msg);
        } catch (LoginException le) {
            reportUnexpectedLoginError("LoginException", le);
        }
        return null;
        
        
    }
    
    
    private void sendForward(HttpServletRequest request, 
                             HttpServletResponse response,
                             String forwardUrl){
        
      _logger.info("Foward URI to " + forwardUrl);  
      FacesContext ctx = FacesContext.getCurrentInstance();
      RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
      try {
        dispatcher.forward(request, response);
      } catch (ServletException se) {
        reportUnexpectedLoginError("ServletException", se);
      } catch (IOException ie) {
        reportUnexpectedLoginError("IOException", ie);
     }
      ctx.responseComplete();
     }
    
    private void reportUnexpectedLoginError(String errType, Exception e){
      FacesMessage msg =
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error during login",
                         "Unexpected error during login (" + errType + "), " +
                            "please consult logs for detail");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      e.printStackTrace();
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    public String getUsername() {
        return _username;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    public String getPassword() {
        return _password;
    }
}
