package wid.bmsbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wid.bmsbackend.entity.DeviceMessage;

import java.nio.ByteBuffer;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultProtocolDecoderTest {

    @Test
    @DisplayName("바이트 배열 변환 테스트")
    void convert() {
        // given
        checkShortValue(new byte[]{0x00, 0x01}, 1);
        checkShortValue(new byte[]{0x00, 0x02}, 2);
        checkShortValue(new byte[]{0x00, 0x02, 0x03, 0x04}, 2);
        checkShortValue(new byte[]{0x00, 0x0A}, 10);
        checkShortValue(new byte[]{0x01, 0x00}, 256);
        checkShortValue(new byte[]{0x01, 0x01}, 257);
        checkShortValue(new byte[]{(byte)0xFF, (byte)0xFF}, -1);
        checkShortValue(new byte[]{(byte)0xFF, (byte)0xFE}, -2);

        checkUnsignedShortValue(new byte[]{(byte)0xFF, (byte)0xFE}, 65534);
        checkUnsignedShortValue(new byte[]{(byte)0x00, (byte)0x01}, 1);

    }

    private void checkShortValue(byte[] array, int expectedValue) {
        int res = ByteBuffer.allocate(2).put(array, 0, 2).flip().getShort();
        assertThat(res).isEqualTo(expectedValue);
    }

    private void checkUnsignedShortValue(byte[] array, int expectedValue) {

        int res = ByteBuffer.allocate(2).put(array, 0, 2).flip().getShort() & 0xFFFF;
        assertThat(res).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("바이트어레이의 값을 DeviceMessage 객체로 변환한다.")
    void decode() {
        // given
        byte[] arrays = new byte[]{0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01};
        String message = Base64.getEncoder().encodeToString(arrays);
        ProtocolDecoder protocolDecoder = new DefaultProtocolDecoder();

        // when
        DeviceMessage deviceMessage = protocolDecoder.decode(message);
        // then
        assertThat(deviceMessage.getTemp()).isEqualTo(0.1);
        assertThat(deviceMessage.getCurrent()).isEqualTo(0.01);
        assertThat(deviceMessage.getVoltage()).isEqualTo(0.1);
        assertThat(deviceMessage.getImpedance()).isEqualTo(0.1);



    }
}