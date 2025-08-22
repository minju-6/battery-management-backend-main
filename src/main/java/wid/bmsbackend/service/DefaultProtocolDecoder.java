package wid.bmsbackend.service;

import org.springframework.stereotype.Service;
import wid.bmsbackend.entity.DeviceMessage;
import wid.bmsbackend.exception.InvalidProtocolFormatException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DefaultProtocolDecoder implements ProtocolDecoder {
    @Override
    public DeviceMessage decode(String payload) {
        byte[] originData = Base64.getDecoder().decode(payload);

        byte[] tempArray = Arrays.copyOfRange(originData, 0, 2);
        byte[] currentArray = Arrays.copyOfRange(originData, 2, 4);
        byte[] voltageArray = Arrays.copyOfRange(originData, 4, 6);
        byte[] impedanceArray = Arrays.copyOfRange(originData, 6, 8);

        double temp = (double) shortValue(tempArray) / 10;
        double current = (double) unsignedShortValue(currentArray) / 100;
        double voltage = (double) unsignedShortValue(voltageArray) / 10;
        double impedance = (double) unsignedShortValue(impedanceArray) / 10;

        return DeviceMessage.builder()
                .temp(temp)
                .current(current)
                .voltage(voltage)
                .impedance(impedance)
                .build();
    }

}
