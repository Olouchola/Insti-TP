package io.artcreativity.monpatrick.ui;

public interface AuthCallback {

    void sendMessage(String phoneNumber);
    void verification(String code);

}
