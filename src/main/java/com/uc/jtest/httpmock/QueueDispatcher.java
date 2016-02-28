    /*
     * Copyright (C) 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    package com.uc.jtest.httpmock;

    import java.net.HttpURLConnection;
    import java.util.concurrent.BlockingQueue;
    import java.util.concurrent.LinkedBlockingQueue;

    /**
     * Default dispatcher that processes a script of responses.  Populate the script by calling
     * {@link #enqueueResponse(MockResponse)}.
     */
    public class QueueDispatcher extends Dispatcher {
        protected final BlockingQueue<MockResponse> responseQueue
                = new LinkedBlockingQueue<MockResponse>();
        private MockResponse failFastResponse;
        private boolean faviconSocketPolicyDisconnect =false;

        @Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            // to permit interactive/browser testing, ignore requests for favicons
            final String requestLine = request.getRequestLine();
            if (requestLine != null && requestLine.equals("GET /favicon.ico HTTP/1.1")) {
                System.out.println("served " + requestLine);
                if (faviconSocketPolicyDisconnect ==false){
                	return new MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
                }else{
                	return new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    .setSocketPolicy(SocketPolicy.DISCONNECT_AT_END);
                	
                }
            }

            setFailFast(true);
            if (failFastResponse != null && responseQueue.peek() == null) {
                // Fail fast if there's no response queued up.
                return failFastResponse;
            }

            return responseQueue.take();
        }

        @Override public SocketPolicy peekSocketPolicy() {
            MockResponse peek = responseQueue.peek();
            if (peek == null) {
                return failFastResponse != null
                        ? failFastResponse.getSocketPolicy()
                        : SocketPolicy.KEEP_OPEN;
            }
            return peek.getSocketPolicy();
        }
        
        public void setfaviconSocketPolicyDisconnect(boolean SocketPolicyDisconnect ){
        	faviconSocketPolicyDisconnect =SocketPolicyDisconnect;
        }

        public void enqueueResponse(MockResponse response) {
            responseQueue.add(response);
        }

        public void enqueueResponse(MockResponse response, String type) {
        	if(type =="html"){
        		response.addHeader("Content-Type","text/html;charset=UTF-8");
        	}else if (type =="css"){
        		response.addHeader("Content-Type","text/css");
        	}else if(type =="js"){
        		response.addHeader("Content-Type","application/x-javascript");
        	}else if(type =="jpeg"){
        		response.addHeader("Content-Type","image/jpeg");
        	}else if(type =="png"){
        		response.addHeader("Content-Type","image/png");
        	}else if(type =="xml"){
        		response.addHeader("Content-Type","text/xml");
        	}
        	
        	responseQueue.add(response);
        	
        }
        
        public void setFailFast(boolean failFast) {
            MockResponse failFastResponse = failFast
                    ? new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    : null;
            setFailFast(failFastResponse);
        }

        public void setFailFast(MockResponse failFastResponse) {
            this.failFastResponse = failFastResponse;
        }
    }