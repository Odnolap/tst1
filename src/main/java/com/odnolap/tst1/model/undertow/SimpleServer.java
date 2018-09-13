package com.odnolap.tst1.model.undertow;

import com.odnolap.tst1.helper.PropertiesHelper;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleServer {
    private static final int PORT = PropertiesHelper.getProperty("port", 8080);
    private static final String HOST = PropertiesHelper.getProperty("host", "localhost");

    private final Undertow.Builder undertowBuilder;

    private SimpleServer(Undertow.Builder undertow) {
        this.undertowBuilder = undertow;
    }

    public Undertow.Builder getUndertow() {
        return undertowBuilder;
    }

    public Undertow start() {
        Undertow undertow = undertowBuilder.build();
        undertow.start();
        //  Undertow logs this on debug but we get logs only from info level so we log it here.
        undertow.getListenerInfo()
            .forEach(listenerInfo -> log.info(listenerInfo.toString()));
        return undertow;
    }

    public static SimpleServer simpleServer(HttpHandler handler) {
        Undertow.Builder undertow = Undertow.builder()
            /*
             * This setting is needed if you want to allow '=' as a value in a cookie.
             * If you base64 encode any cookie values you probably want it on.
             */
            .setServerOption(UndertowOptions.ALLOW_EQUALS_IN_COOKIE_VALUE, true)
            .setServerOption(UndertowOptions.SHUTDOWN_TIMEOUT, 0)
            .addHttpListener(PORT, HOST, handler)
            ;
        return new SimpleServer(undertow);
    }
}
