package com.example.app.top;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.apiai.ApiaiWebhookRequest;
import com.example.domain.polly.PollyService;
import com.google.gson.Gson;

import ai.api.GsonFactory;
import ai.api.model.Result;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TopController {
	
	private final PollyService pollyService;
	private final TopService topService;
	
	
	public TopController(PollyService pollyService, TopService topService) {
		this.pollyService = pollyService;
		this.topService = topService;
	}
	
	@RequestMapping(value = "/")
	public String index() {
		return "top";
	}
	
	@GetMapping(value = "/speech")
	public ResponseEntity<byte[]> getVoiceData(@RequestParam (value = "text", required = true) String text) {
		HttpHeaders headers = new HttpHeaders();
		try {
			return new ResponseEntity<>(
							pollyService.synthesize(text),
							headers,
							HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(
							null,
							headers,
							HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ResponseBody
	@GetMapping(value = "/question")
	public String fetchAnswer(HttpServletRequest request, @RequestParam (value = "text", required = true) String text) {
		return topService.getReply(text, request.getSession().getId());
	}
	
	private final Gson gson = GsonFactory.getDefaultFactory().getGson();
	
	@ResponseBody
	@PostMapping(value="/webhook")
	public Result webhook(HttpServletRequest request) {
		
		try {
			ApiaiWebhookRequest req = gson.fromJson(request.getReader(), ApiaiWebhookRequest.class);
			return req.getResult();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
}