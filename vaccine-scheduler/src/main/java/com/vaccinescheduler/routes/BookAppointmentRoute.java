package com.vaccinescheduler.routes;

import com.vaccinescheduler.dtos.other.AppointmentData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookAppointmentRoute extends RouteBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(BookAppointmentRoute.class);
    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(AppointmentData.class);

//        from("quartz://notifyPatientTimer?cron=0+46+18+1/1+*+?+*") // Run every hour
//                .pollEnrich("file:src/main/resources/csv?fileName=appointmentDetails.csv&noop=true")
//                .unmarshal(bindyCsvDataFormat)
//                .split(body())
//                    .to("direct:sendConfirmationEmail")
//                .end()
//                .log("SendConfirmationEmail process done.");

        from("direct:sendConfirmationEmail")
                .log("sendConfirmationEmail route started.")
                .process(exchange -> {
                    AppointmentData appointmentData = exchange.getIn().getBody(AppointmentData.class);
                    if(!appointmentData.getNotified()) {
                        String patientEmail = appointmentData.getPatientEmail();
                        exchange.setProperty("email", patientEmail);
                        String subject = "Appointment confirmation from ~ [ " + appointmentData.getHospitalName() + " ]";
                        exchange.setProperty("subject", subject);
                        LOGGER.info("Preparing email template for patient email : " + patientEmail);
                        exchange.setProperty("originalObject", appointmentData);
                    }
                })
                .to("velocity:templates/bookAppointment.vm")
                .setHeader("To", simple("${exchangeProperty.email}"))
                .setHeader("From", constant("gauravdhamal11@gmail.com"))
                .setHeader("Subject", simple("${exchangeProperty.subject}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .to("smtps://smtp.gmail.com:465?username=gauravdhamal11@gmail.com&password=lhvsnnkmmjyyxxch")
                .log("Booking confirmation email sent successfully to : ${exchangeProperty.email}");
    }
}
