package com.vaccinescheduler.routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@Component
public class AwsRoute extends RouteBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(AwsRoute.class);
    @Override
    public void configure() throws Exception {
        from("quartz://uploadToS3Daily?cron=0+59+23+?+*+MON,TUE,WED,THU,FRI,SAT,SUN+*")
//        from("timer://UploadToS3Daily?repeatCount=1")
                .process(exchange -> LOGGER.info("uploadToS3Daily route started at : ${date:now:yyyy-MM-dd HH:mm:ss}"))
                .setHeader("CamelAwsS3Key", simple("vaccinationData_${date:now:dd-MM-yyyy}.csv"))
                .log("File name : vaccinationData_${date:now:dd-MM-yyyy}.csv")
                .process(exchange -> {
                    String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    String filePath = "src/main/resources/csv/vaccinationData_"+formatDate+".csv";
                    File file = new File(filePath);
                    exchange.getIn().setBody(file);
                })
                .log("Body contains : ${body}.")
                .to("aws2-s3://vaccination-data-temp?accessKey=AKIAQH3WNZ5U6WVQQRPK&secretKey=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")
                .log("File uploaded to aws s3 bucket.");
    }
}
