package com.application.vpp.Interfaces;

public interface ConnectionProcess {
    public void connected();
    public void serverNotAvailable();
    public void IntenrnetNotAvailable();
    public void ConnectToserver(ConnectionProcess connectionProcess);
    public void SocketDisconnected();
}
