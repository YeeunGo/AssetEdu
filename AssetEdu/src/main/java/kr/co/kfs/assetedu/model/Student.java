package kr.co.kfs.assetedu.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Student {
	private String id;
	private String name;
	private Integer age;
	private Double weight;
	private Long asset;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth;
}
