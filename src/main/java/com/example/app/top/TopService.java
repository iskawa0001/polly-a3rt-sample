package com.example.app.top;

import org.springframework.stereotype.Service;

import com.example.domain.a3rt.A3rtService;
import com.example.domain.apiai.ApiaiService;

import ai.api.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopService {
	
	private final A3rtService a3rtService;
	private final ApiaiService apiaiService;
	
	public TopService(A3rtService a3rtService, ApiaiService apiaiService) {
		this.a3rtService = a3rtService;
		this.apiaiService = apiaiService;
	}
	
	
	public String getReply(String question, String sessionId) {
		if(StringUtils.isEmpty(question)) return "もう一度お願いします。";
		
		// apiai優先
		try {
			String userAction = apiaiService.getUsersAction(question, sessionId);
			
			if(!apiaiService.isInputUnknown(userAction)) {
				// 入力値がapiaiで理解できた場合は固定値返す
				return apiaiService.ActionToSpeech(userAction);
			}
		} catch (Exception e) {
			// 例外が起きてもa3rtに処理を引き継ぐよ
			log.error(e.getMessage(), e);
		}
		
		// apiaiで処理できなかった場合
		// a3rtの処理
		try {
			return a3rtService.getReply(question);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "もう一度よろしいですか？";
		}
	}
}
