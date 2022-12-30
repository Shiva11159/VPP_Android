package com.application.vpp.Interfaces;

import java.io.Serializable;

public interface NetProcess extends Serializable {

    /**
     * process method is implemented  by a class
     *
     * @param result             String which passed the arguments
     * @param http_response_code if ok Then it is 200 or java.net.HttpURLConnection.HTTP_OK or else it is an error
     *                           there is no return type ie void
     */
    public void process(String result, int http_response_code);

}
