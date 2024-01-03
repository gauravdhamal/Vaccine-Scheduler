package com.vaccinescheduler.routes;

import com.vaccinescheduler.dtos.other.VaccinationData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateVaccinationRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(UpdateVaccinationRoute.class);
    @Override
    public void configure() throws Exception {
        from("direct:updateVaccinationRecord")
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
                .setHeader("To", simple("${exchangeProperty.email}"))
                .setHeader("From", constant("gauravdhamal11@gmail.com"))
                .setHeader("Subject", simple("${exchangeProperty.subject}"))
                .setHeader(Exchange.CONTENT_TYPE, simple("text/plain"))
                .to("smtps://smtp.gmail.com:465?username=gauravdhamal11@gmail.com&password=lhvsnnkmmjyyxxch")
                .log("Vaccination completed email sent successfully to : ${exchangeProperty.email}");
    }
}
