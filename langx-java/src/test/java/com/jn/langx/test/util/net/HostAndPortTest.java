package com.jn.langx.test.util.net;

import com.jn.langx.util.net.HostAndPort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HostAndPortTest {

    // 测试空值场景
    @Test
    public void testOf_BlankHost_ReturnsNull() {
        assertNull(HostAndPort.of(null));
        assertNull(HostAndPort.of(""));
        assertNull(HostAndPort.of("   "));
    }

    // 测试双引号去除逻辑
    @Test
    public void testOf_QuotedHost_StripsQuotes() throws Exception {
        HostAndPort hp = HostAndPort.of("\"example.com\"");
        assertEquals("example.com", hp.getName());
    }

    // 测试标准 IPv4 地址
    @Test
    public void testOf_Ipv4WithPort() {
        HostAndPort hp = HostAndPort.of("localhost:8080");
        assertEquals("localhost", hp.getName());
        assertEquals(8080L, (long) hp.getValue());
    }

    // 测试 IPv6 地址带端口号
    @Test
    public void testOf_IPv6WithPort() {
        HostAndPort hp = HostAndPort.of("[::1]:8080");
        assertEquals("[::1]", hp.getName());
        assertEquals(8080, (long) hp.getValue());
    }

    // 测试 IPv6 无端口号
    @Test
    public void testOf_IPv6WithoutPort() {
        HostAndPort hp = HostAndPort.of("[2001:db8::1]");
        assertEquals("[2001:db8::1]", hp.getName());
        assertEquals(-1, (long) hp.getValue());
    }

    // 测试非法端口号格式
    @Test(expected = NumberFormatException.class)
    public void testOf_InvalidPort_ThrowsException() {

        HostAndPort.of("localhost:invalid");
    }

    // 测试无端口号场景
    @Test
    public void testOf_HostWithoutPort() {
        HostAndPort hp = HostAndPort.of("example.com");
        assertEquals("example.com", hp.getKey());
        assertEquals(-1, (long) hp.getValue());
    }

}
