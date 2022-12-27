package com.abhidutta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class EmailDetails {
	@Id
	@GeneratedValue
	private Integer id;
	private String fromDetails;
	private String toDetails;
	private String msgBody;
	private String subject;
}
