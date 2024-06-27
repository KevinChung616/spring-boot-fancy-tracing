package com.jtsp.springboottracingfancy;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@Slf4j
public class SpringBootTracingFancyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTracingFancyApplication.class, args);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${target.port}")
    private String targetPort;

    @Value("${server.port}")
    private int serverPort;


    @GetMapping("/hello")
    public ResponseEntity<String> handleRequest() {
        if (StringUtils.isEmpty(targetPort) || targetPort.equals("END")) {
            log.info("end of the request chain");
            return ResponseEntity.ok("success, end of request @ port : " + serverPort);
        } else {
            log.info("middle of the request chain");
            String[] ports = targetPort.split(",");
            for (String port : ports) {
                String url = "http://localhost:" + port + "/hello";
                restTemplate.getForObject(url, String.class);
            }
            return ResponseEntity.ok("success, continue request to next ports" + ports);
        }

    }

}
