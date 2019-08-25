package com.github.gilbertotcc.playground.websocket;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CryptoCompareWebSocketClient {

  public static final String WEB_SOCKET_URL = "https://streamer.cryptocompare.com/";
  public static final String SUBSCRITION_LABEL_0 = "5~CCCAGG~XMG~BTC";
  public static final String SUBSCRITION_LABEL_1 = "5~CCCAGG~ETH~BTC";

  public static void main(String... args) throws Exception {
    JSONObject obj = new JSONObject();
    obj.put("subs", new String[]{SUBSCRITION_LABEL_0, SUBSCRITION_LABEL_1});

    Socket socket = IO.socket(WEB_SOCKET_URL);
    socket.on(Socket.EVENT_CONNECT, objs -> {
      System.out.println("Subscribing...");
      socket.emit("SubAdd", obj);
    })
      .on("m", objs -> parseResponse(objs).forEach(o -> System.out.println(ReflectionToStringBuilder.toString(o, ToStringStyle.JSON_STYLE))));
    socket.connect();
  }

  public static final List<Object> parseResponse(Object... objects) {
    return Stream.of(objects)
      .map(String.class::cast)
      .map(s -> StringUtils.split(s, "~"))
      .collect(Collectors.toList());
  }
}
