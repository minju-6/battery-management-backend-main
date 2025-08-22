package wid.bmsbackend.service;

import wid.bmsbackend.entity.DeviceMessage;

public interface ProtocolDecoder {
    default int shortValue(byte[] bytes) {
        return (bytes[0] << 8) | (bytes[1] & 0xFF);
    }
    default int unsignedShortValue(byte[] bytes) {
        return shortValue(bytes) & 0xFFFF;
    }

    DeviceMessage decode(String message);
}
