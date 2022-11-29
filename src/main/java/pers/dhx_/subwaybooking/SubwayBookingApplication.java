package pers.dhx_.subwaybooking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("pers.dhx_.subwaybooking.mapper")
public class SubwayBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayBookingApplication.class, args);
    }

}

