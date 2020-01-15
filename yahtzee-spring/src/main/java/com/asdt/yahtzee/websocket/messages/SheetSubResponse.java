package com.asdt.yahtzee.websocket.messages;

import java.util.Map;

public class SheetSubResponse {

	private Map<String, Integer> content = null;

	public SheetSubResponse() {
	}

	public SheetSubResponse(Map<String, Integer> content) {
		this.content = content;
	}

	public Map<String, Integer> getContent() {
		return content;
	}
}
