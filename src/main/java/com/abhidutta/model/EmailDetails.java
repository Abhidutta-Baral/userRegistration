package com.abhidutta.model;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class EmailDetails {
	private String fromDetails;
	private String toDetails;
	private String msgBody;
	private String subject;
}
