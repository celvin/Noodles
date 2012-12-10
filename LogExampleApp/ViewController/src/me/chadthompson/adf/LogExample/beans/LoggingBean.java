package me.chadthompson.adf.LogExample.beans;

import javax.faces.event.ActionEvent;

import oracle.adf.share.logging.ADFLogger;

// Making a secondary change.  I can continue working on my branch.
// A sipmle change.

public class LoggingBean {
    
    private ADFLogger _logger = 
        ADFLogger.createADFLogger(LoggingBean.class);
    

    public void logErrorMessage(ActionEvent actionEvent) {
        // Add event code here...
        _logger.log(ADFLogger.ERROR, "Error Button Pressed");
    }

    public void logWarningMessage(ActionEvent actionEvent) {
        // Add event code here..
        _logger.log(ADFLogger.WARNING, "Warning Button Pressed");
    }

    public void logNotificationMessage(ActionEvent actionEvent) {
        // Add event code here...
        _logger.log(ADFLogger.NOTIFICATION, "Notification Button Pressed");
    }

    public void logTraceMessage(ActionEvent actionEvent) {
        // Add event code here...
        _logger.log(ADFLogger.TRACE, "Trace Button Pressed");
    }
}
