package com.vaccinescheduler.routes;

import com.vaccinescheduler.dtos.other.AppointmentData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RescheduleAppointmentRoute extends RouteBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(RescheduleAppointmentRoute.class);
    @Override
    public void configure() throws Exception {
        from("direct:sendRescheduleEmail")
                .process(exchange -> LOGGER.info("sendRescheduleEmail route started."))
                .process(exchange -> {
                    AppointmentData appointmentData = exchange.getIn().getBody(AppointmentData.class);
                    String patientEmail = appointmentData.getPatientEmail();
                    String subject = "Rescheduled appointment confirmation from ~ [ " + appointmentData.getHospitalName() + " ]";
                    exchange.setProperty("email", patientEmail);
                    exchange.setProperty("subject", subject);
                    LOGGER.info("Preparing email template for patient email : " + patientEmail);
                })
                .to("velocity:templates/rescheduleAppointment.vm")
                .setHeader("To", simple("${exchangeProperty.email}"))
                .setHeader("From", constant("gauravdhamal11@gmail.com"))
                .setHeader("subject", simple("${exchangeProperty.subject}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .to("smtps://smtp.gmail.com:465?username=gauravdhamal11@gmail.com&password=lhvsnnkmmjyyxxch")
                .log("Reschedule email sent successfully to : ${exchangeProperty.email}");
    }
}
