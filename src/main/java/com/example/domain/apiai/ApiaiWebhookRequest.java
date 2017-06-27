package com.example.domain.apiai;

import java.io.Serializable;
import java.util.Map;

import ai.api.model.AIResponse;

public class ApiaiWebhookRequest extends AIResponse {
	
	private static final long serialVersionUID = 1L;

	private OriginalRequest originalRequest;

	/**
	 * Get original request object
	 * @return <code>null</code> if original request undefined in
	 * request object
	 */
	public OriginalRequest getOriginalRequest() {
		return originalRequest;
	}
	
	
	
	
	/**
	 * Original request model class
	 */
	protected static class OriginalRequest implements Serializable {
		private static final long serialVersionUID = 1L;
		private String source;
		private Map<String, ?> data;
	
		/**
		 * Get source description string
		 * @return <code>null</code> if source isn't defined in a request
		 */
		public String getSource() {
			return source;
		}
	
		/**
		 * Get data map
		 * @return <code>null</code> if data isn't defined in a request
		 */
		public Map<String, ?> getData() {
			return data;
		}
	}
}
