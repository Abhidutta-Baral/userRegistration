package com.abhidutta.dto;

import lombok.Data;

@Data
public class UnlockAccForm {
	private String email;
	private String tempPass;
	private String newPass;
	private String confPass;
}
