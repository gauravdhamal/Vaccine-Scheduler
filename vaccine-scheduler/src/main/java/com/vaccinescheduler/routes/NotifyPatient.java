package com.vaccinescheduler.routes;

import com.vaccinescheduler.dtos.other.AppointmentData;
import com.vaccinescheduler.dtos.other.PaymentData;
import com.vaccinescheduler.dtos.other.VaccinationData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifyPatient extends RouteBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(NotifyPatient.class);
    @Override
    public void configure() throws Exception {
        from("seda:sendConfirmationEmail")
                .process(exchange -> LOGGER.info("sendConfirmationEmail route started."))
                .process(exchange -> {
                    AppointmentData appointmentData = exchange.getIn().getBody(AppointmentData.class);
                    String patientEmail = appointmentData.getPatientEmail();
                    String hospitalName = appointmentData.getHospitalName();
                    String subject = "Appointment confirmation from ~ [ " + hospitalName + " ]";
                    exchange.setProperty("email", patientEmail);
                    exchange.setProperty("subject", subject);
                    LOGGER.info("Preparing email template for patient email : " + patientEmail);
                })
                .to("velocity:templates/bookAppointment.vm")
                .to("direct:sendEmail")
                .log("Booking confirmation email sent successfully to : ${exchangeProperty.email}");

        from("seda:sendRescheduleEmail")
                .process(exchange -> LOGGER.info("sendRescheduleEmail route started."))
                .process(exchange -> {
                    AppointmentData appointmentData = exchange.getIn().getBody(AppointmentData.class);
                    String patientEmail = appointmentData.getPatientEmail();
                    String hospitalName = appointmentData.getHospitalName();
                    String subject = "Rescheduled appointment confirmation from ~ [ " + hospitalName + " ]";
                    exchange.setProperty("email", patientEmail);
                    exchange.setProperty("subject", subject);
                    LOGGER.info("Preparing email template for patient email : " + patientEmail);
                })
                .to("velocity:templates/rescheduleAppointment.vm")
                .to("direct:sendEmail")
                .log("Reschedule email sent successfully to : ${exchangeProperty.email}");

        from("seda:processPaymentNotification")
                .process(exchange -> LOGGER.info("processPaymentNotification route started."))
                .process(exchange -> {
                    PaymentData paymentData = exchange.getIn().getBody(PaymentData.class);
                    String patientEmail = paymentData.getPatientEmail();
                    String hospitalName = paymentData.getHospitalName();
                    String subject = "Payment confirmation mail from ~ [ "+hospitalName+" ]";
                    exchange.setProperty("email", patientEmail);
                    exchange.setProperty("subject", subject);
                    LOGGER.info("Preparing email template for patient email : " + patientEmail);
                })
                .to("velocity:templates/paymentConfirmation.vm")
                .to("direct:sendEmail")
                .log("Payment confirmation email sent successfully to : ${exchangeProperty.email}");

        from("seda:updateVaccinationRecord")
                .process(exchange -> LOGGER.info("updateVaccinationRecord route started."))
                .process(exchange -> {
                    VaccinationData vaccinationData = exchange.getIn().getBody(VaccinationData.class);
                    String patientEmail = vaccinationData.getPatientEmail();
                    String hospitalName = vaccinationData.getHospitalName();
                    String subject = "Vaccination confirmation mail from ~ [ "+hospitalName+" ]";
                    exchange.setProperty("email", patientEmail);
                    exchange.setProperty("subject", subject);
                    LOGGER.info("Preparing email template for patient email : " + patientEmail);
                })
                .to("velocity:templates/updateVaccination.vm")
                .to("direct:sendEmail")
                .log("Vaccination completed email sent successfully to : ${exchangeProperty.email}");

        from("direct:sendEmail")
                .setHeader("To", simple("${exchangeProperty.email}"))
                .setHeader("From", constant("gauravdhamal11@gmail.com"))
                .setHeader("Subject", simple("${exchangeProperty.subject}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .to("smtps://smtp.gmail.com:465?username=gauravdhamal11@gmail.com&password=lhvsnnkmmjyyxxch")
                .end();
    }
}
