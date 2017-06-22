package com.bugtrapper01.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.bugtrapper01.transfer.util.TimerUtil;
@SpringBootApplication
public class Bugtrapper01Application {

	public static void main(String[] args) {
		SpringApplication.run(Bugtrapper01Application.class, args);
		TimerUtil.Timer();

	}

}
