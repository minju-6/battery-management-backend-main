package wid.bmsbackend.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EuiTest {
    @Test
    void Test() {
        BigDecimal v = BigDecimal.valueOf(0x70b3d50032100020L);
        BigDecimal v1 = BigDecimal.valueOf(0x70b3d50032100021L);
        BigInteger bigInteger = v.add(new BigDecimal(1)).toBigInteger();
        BigInteger startInteger = v.toBigInteger();
        String startEui = startInteger.toString(16);
        String eui = bigInteger.toString(16);

        System.out.println("v : " + v);
        System.out.println("v1 : " + v1);
        System.out.println("bigInteger : " + bigInteger);
        System.out.println("startEui = " + startEui);
        System.out.println("eui : " + eui);
    }
}
