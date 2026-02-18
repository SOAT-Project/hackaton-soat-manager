package soat.project.hackaton_soat_manager.domain.exception;

import soat.project.hackaton_soat_manager.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification aNotification) {
        super(aMessage, aNotification.getErrors());
    }

}
