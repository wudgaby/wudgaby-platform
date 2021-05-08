package com.wudgaby.retrofit.springboot.demo.github;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Contributor {
	private String login;
	private int contributions;
}
