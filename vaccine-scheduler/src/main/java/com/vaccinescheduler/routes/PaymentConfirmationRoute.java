package com.vaccinescheduler.routes;

import com.vaccinescheduler.dtos.other.PaymentData;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class PaymentConfirmationRoute extends RouteBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationRoute.class);
    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(PaymentData.class);

        from("file:src/main/resources/csv?fileName=paymentDetails.csv&noop=true")
                .unmarshal(bindyCsvDataFormat)
                .split(body())
                    .to("direct:processPaymentNotification")
                .end()
                .log("processPaymentNotification process done.");

        from("direct:processPaymentNotification")
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
                .setHeader("To", simple("${exchangeProperty.email}"))
                .setHeader("From", constant("gauravdhamal11@gmail.com"))
                .setHeader("subject", simple("${exchangeProperty.subject}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .to("smtps://smtp.gmail.com:465?username=gauravdhamal11@gmail.com&password=lhvsnnkmmjyyxxch")
                .log("Payment confirmation email sent successfully to : ${exchangeProperty.email}");
    }
}
