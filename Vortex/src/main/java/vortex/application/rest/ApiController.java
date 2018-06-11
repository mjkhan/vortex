package vortex.application.rest;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vortex.application.ApplicationController;

@RestController
@RequestMapping("/api")
public class ApiController extends ApplicationController {
	@RequestMapping("/test")
	public HashMap<String, Object> test() {
		log().debug(() -> "Executing ApiController.test()...");
		HashMap<String, Object> result = new HashMap<>();
		result.put("greeting", "hello");
		result.put("target", "world!");
		return result;
	}
}