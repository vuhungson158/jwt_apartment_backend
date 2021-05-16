package com.hung91hn.apartment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	@RequestMapping("/")
	public String get() {
		return "Service Apartment đang chạy.";
	}
}
