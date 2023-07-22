package com.wayyeasy.wayyeasy.ComponentFiles;

import android.content.Context;
import android.util.Log;

import com.wayyeasy.wayyeasy.Models.Hospitals.bookings.room_book_request_user_to_owner;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketHandler implements Runnable {
    private static final String TAG = "Socket handler";

    room_book_request_user_to_owner bookingData;
    Context context;

    public SocketHandler(List<room_book_request_user_to_owner> bookingDataArray, Context context) {
        this.bookingData = bookingDataArray.get(0);
        this.context = context;
    }

    @Override
    public void run() {
        final Socket mSocket;

        try {
            mSocket = IO.socket("http://192.168.0.102:2002");

            Gson gson = new Gson();
            String jsonString = gson.toJson(bookingData);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    mSocket.emit("userBookRequest", jsonString);
//                    mSocket.disconnect();
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                }

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "Error connect");
                }
            }).on("connection", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "call: connect Successful ");
                }
            });

            mSocket.connect();
        } catch (URISyntaxException UTE) {
            Log.e(TAG, UTE.toString());
        }
    }
}