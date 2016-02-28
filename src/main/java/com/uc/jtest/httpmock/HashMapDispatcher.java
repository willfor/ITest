package com.uc.jtest.httpmock;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class HashMapDispatcher extends Dispatcher {

	protected final HashMap<String, MockResponse> responseHashMap =
			new HashMap<String, MockResponse>();
	
	@Override
	public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
		// TODO Auto-generated method stub
		final String requestPath = request.getPath();
		if (responseHashMap.containsKey(requestPath)){
			return responseHashMap.get(requestPath);
			
		}else{
			return  new MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
		}
	}
	
    public void putResponse(String path, MockResponse response) {
    	responseHashMap.put(path, response);
    }

}
