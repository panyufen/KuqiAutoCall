package com.example.pan.autocall.http;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by PAN on 2017/12/20.
 */
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("{");
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                stringBuffer.append(formBody.name(i)).append(":").append(formBody.value(i));
                if (i + 1 < formBody.size()) {
                    stringBuffer.append(",");
                }
            }
        } else {
            stringBuffer.append("...MultipartBody...");
        }
        stringBuffer.append("}");

        long t1 = System.nanoTime();
//        Logger.i(String.format("Sending request %s%nbody:%s", request.url(), stringBuffer.toString()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        ResponseBody responseBody = response.body();
        long contentLen = responseBody.contentLength();
        String result = "";

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

//        Charset charset = Util.bomAwareCharset(buffer, response.body().contentType().charset());
//
        if (contentLen != 0) {
            result = buffer.clone().readString(Charset.forName("utf-8"));
            result = decodeUnicode(result);
        }

//        Logger.i(String.format("Received response from %s in %.1fms%nbody:%s", response.request().url(), (t2 - t1) / 1e6d, result + " \nlen:" + result.length()));

        return response;
    }


    private String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}