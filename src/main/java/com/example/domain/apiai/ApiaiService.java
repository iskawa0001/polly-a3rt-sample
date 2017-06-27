package com.example.domain.apiai;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

@Service
public class ApiaiService {
	
	private AIConfiguration aiConfig;
	private AIDataService aiDataService;
	
	// テストなので~/.spring-boot-devtools.propertiesに用意
	@Value("${apiai.apikey}")
	private String API_KEY;
	
	private final ApiaiKuniponProperties apiaiKuniponProperties;
	
	/** default fallback */
	private static final String INPUT_UNKNOWN = "input.unknown";
	
	public ApiaiService(ApiaiKuniponProperties apiaiKuniponProperties) {
		this.apiaiKuniponProperties = apiaiKuniponProperties;
	}
	
	@PostConstruct
	public void aiInit() {
		this.aiConfig = new AIConfiguration(API_KEY);
		this.aiDataService = new AIDataService(this.aiConfig);
	}
	
	/**
	 * ユーザーがどういう意味の質問をしてきたかを取得
	 * @param question
	 * @return
	 * @throws Exception
	 */
	public String getUsersAction(String question, String sessionId) throws Exception {
		AIServiceContext aiServiceContext = (sessionId != null) ? AIServiceContextBuilder.buildFromSessionId(sessionId) : null;
		
		AIRequest request = new AIRequest(question);
		AIResponse response = aiDataService.request(request, aiServiceContext);
		
		if(response.getStatus().getCode() == 200) {
			return response.getResult().getAction();
		} else {
			return "input.unknown";
		}
	}
	
	
	public String ActionToSpeech(String fullfilmentAction) {
		return apiaiKuniponProperties.getSpeech(fullfilmentAction);
	}
	
	public boolean isInputUnknown(String fullfilmentAction) {
		return StringUtils.equals(fullfilmentAction, INPUT_UNKNOWN);
	}
	
	public String test(String question) throws Exception {
		AIRequest request = new AIRequest(question);
		AIResponse response = aiDataService.request(request);
		if(response.getStatus().getCode() == 200) {
			String fullfilmentAction = response.getResult().getAction();
			System.out.println(fullfilmentAction);
			System.out.println(apiaiKuniponProperties.getSpeech(fullfilmentAction));
			return response.getResult().getFulfillment().getSpeech();
		} else {
			System.out.println(response.getStatus().getErrorDetails());
//			throw new RuntimeException("ごめんなさい。今、調子が悪いの。");
			return "ごめんなさい。今、調子が悪いの。";
		}
	}
}
