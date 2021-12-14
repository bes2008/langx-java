/*
 * Copyright (c) 2006, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.jn.langx.security.jdkjssejar.sun.security.ssl;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import java.security.spec.ECParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.AlgorithmParameters;

import com.jn.langx.security.jdkjssejar.sun.security.util.CryptoPrimitive;
import com.jn.langx.security.jdkjssejar.sun.security.util.AlgorithmConstraints;

import java.security.AccessController;
import java.util.EnumSet;
import java.util.ArrayList;
import javax.net.ssl.SSLProtocolException;

import sun.security.action.GetPropertyAction;

/**
 * This file contains all the classes relevant to TLS Extensions for the
 * ClientHello and ServerHello messages. The extension mechanism and
 * several extensions are defined in RFC 3546. Additional extensions are
 * defined in the ECC RFC 4492.
 * <p>
 * Currently, only the two ECC extensions are fully supported.
 * <p>
 * The classes contained in this file are:
 * . HelloExtensions: a List of extensions as used in the client hello
 * and server hello messages.
 * . ExtensionType: an enum style class for the extension type
 * . HelloExtension: abstract base class for all extensions. All subclasses
 * must be immutable.
 * <p>
 * . UnknownExtension: used to represent all parsed extensions that we do not
 * explicitly support.
 * . ServerNameExtension: partially implemented server_name extension.
 * . SupportedEllipticCurvesExtension: the ECC supported curves extension.
 * . SupportedEllipticPointFormatsExtension: the ECC supported point formats
 * (compressed/uncompressed) extension.
 *
 * @author Andreas Sterbenz
 * @since 1.6
 */
final class HelloExtensions {

    private List<HelloExtension> extensions;
    private int encodedLength;

    HelloExtensions() {
        extensions = Collections.emptyList();
    }

    HelloExtensions(HandshakeInStream s) throws IOException {
        int len = s.getInt16();
        extensions = new ArrayList<HelloExtension>();
        encodedLength = len + 2;
        while (len > 0) {
            int type = s.getInt16();
            int extlen = s.getInt16();
            ExtensionType extType = ExtensionType.get(type);
            HelloExtension extension;
            if (extType == ExtensionType.EXT_SERVER_NAME) {
                extension = new ServerNameExtension(s, extlen);
            } else if (extType == ExtensionType.EXT_ELLIPTIC_CURVES) {
                extension = new SupportedEllipticCurvesExtension(s, extlen);
            } else if (extType == ExtensionType.EXT_EC_POINT_FORMATS) {
                extension =
                        new SupportedEllipticPointFormatsExtension(s, extlen);
            } else if (extType == ExtensionType.EXT_RENEGOTIATION_INFO) {
                extension = new RenegotiationInfoExtension(s, extlen);
            } else if (extType == ExtensionType.EXT_EXTENDED_MASTER_SECRET) {
                extension = new ExtendedMasterSecretExtension(s, extlen);
            } else {
                extension = new UnknownExtension(s, extlen, extType);
            }
            extensions.add(extension);
            len -= extlen + 4;
        }
        if (len != 0) {
            throw new SSLProtocolException(
                    "Error parsing extensions: extra data");
        }
    }

    // Return the List of extensions. Must not be modified by the caller.
    List<HelloExtension> list() {
        return extensions;
    }

    void add(HelloExtension ext) {
        if (extensions.isEmpty()) {
            extensions = new ArrayList<HelloExtension>();
        }
        extensions.add(ext);
        encodedLength = -1;
    }

    HelloExtension get(ExtensionType type) {
        for (HelloExtension ext : extensions) {
            if (ext.type == type) {
                return ext;
            }
        }
        return null;
    }

    int length() {
        if (encodedLength >= 0) {
            return encodedLength;
        }
        if (extensions.isEmpty()) {
            encodedLength = 0;
        } else {
            encodedLength = 2;
            for (HelloExtension ext : extensions) {
                encodedLength += ext.length();
            }
        }
        return encodedLength;
    }

    void send(HandshakeOutStream s) throws IOException {
        int length = length();
        if (length == 0) {
            return;
        }
        s.putInt16(length - 2);
        for (HelloExtension ext : extensions) {
            ext.send(s);
        }
    }

    void print(PrintStream s) throws IOException {
        for (HelloExtension ext : extensions) {
            s.println(ext.toString());
        }
    }
}

final class ExtensionType {

    final int id;
    final String name;

    private ExtensionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    static List<ExtensionType> knownExtensions =
            new ArrayList<ExtensionType>(14);

    static ExtensionType get(int id) {
        for (ExtensionType ext : knownExtensions) {
            if (ext.id == id) {
                return ext;
            }
        }
        return new ExtensionType(id, "type_" + id);
    }

    private static ExtensionType e(int id, String name) {
        ExtensionType ext = new ExtensionType(id, name);
        knownExtensions.add(ext);
        return ext;
    }

    // extensions defined in RFC 3546
    final static ExtensionType EXT_SERVER_NAME =
            e(0x0000, "server_name");            // IANA registry value: 0
    final static ExtensionType EXT_MAX_FRAGMENT_LENGTH =
            e(0x0001, "max_fragment_length");    // IANA registry value: 1
    final static ExtensionType EXT_CLIENT_CERTIFICATE_URL =
            e(0x0002, "client_certificate_url"); // IANA registry value: 2
    final static ExtensionType EXT_TRUSTED_CA_KEYS =
            e(0x0003, "trusted_ca_keys");        // IANA registry value: 3
    final static ExtensionType EXT_TRUNCATED_HMAC =
            e(0x0004, "truncated_hmac");         // IANA registry value: 4
    final static ExtensionType EXT_STATUS_REQUEST =
            e(0x0005, "status_request");         // IANA registry value: 5

    // extensions defined in RFC 4681
    final static ExtensionType EXT_USER_MAPPING =
            e(0x0006, "user_mapping");           // IANA registry value: 6

    // extensions defined in RFC 5081
    final static ExtensionType EXT_CERT_TYPE =
            e(0x0009, "cert_type");              // IANA registry value: 9

    // extensions defined in RFC 4492 (ECC)
    final static ExtensionType EXT_ELLIPTIC_CURVES =
            e(0x000A, "elliptic_curves");        // IANA registry value: 10
    final static ExtensionType EXT_EC_POINT_FORMATS =
            e(0x000B, "ec_point_formats");       // IANA registry value: 11

    // extensions defined in RFC 5054
    final static ExtensionType EXT_SRP =
            e(0x000C, "srp");                    // IANA registry value: 12

    // extensions defined in RFC 5246
    final static ExtensionType EXT_SIGNATURE_ALGORITHMS =
            e(0x000D, "signature_algorithms");   // IANA registry value: 13

    // extensions defined in RFC 7627
    static final ExtensionType EXT_EXTENDED_MASTER_SECRET =
            e(0x0017, "extended_master_secret"); // IANA registry value: 23

    // extensions defined in RFC 5746
    final static ExtensionType EXT_RENEGOTIATION_INFO =
            e(0xff01, "renegotiation_info");     // IANA registry value: 65281
}

abstract class HelloExtension {

    final ExtensionType type;

    HelloExtension(ExtensionType type) {
        this.type = type;
    }

    // Length of the encoded extension, including the type and length fields
    abstract int length();

    abstract void send(HandshakeOutStream s) throws IOException;

    public abstract String toString();

}

final class UnknownExtension extends HelloExtension {

    private final byte[] data;

    UnknownExtension(HandshakeInStream s, int len, ExtensionType type)
            throws IOException {
        super(type);
        data = new byte[len];
        // s.read() does not handle 0-length arrays.
        if (len != 0) {
            s.read(data);
        }
    }

    int length() {
        return 4 + data.length;
    }

    void send(HandshakeOutStream s) throws IOException {
        s.putInt16(type.id);
        s.putBytes16(data);
    }

    public String toString() {
        return "Unsupported extension " + type + ", data: " + Debug.toString(data);
    }
}

/*
 * Support for the server_name extension is incomplete. Parsing is implemented
 * so that we get nicer debug output, but we neither send it nor do we do
 * act on it if we receive it.
 */
final class ServerNameExtension extends HelloExtension {

    final static int NAME_HOST_NAME = 0;

    private List<ServerName> names;

    ServerNameExtension(HandshakeInStream s, int len)
            throws IOException {
        super(ExtensionType.EXT_SERVER_NAME);
        names = new ArrayList<ServerName>();
        while (len > 0) {
            ServerName name = new ServerName(s);
            names.add(name);
            len -= name.length + 2;
        }
        if (len != 0) {
            throw new SSLProtocolException("Invalid server_name extension");
        }
    }

    static class ServerName {
        final int length;
        final int type;
        final byte[] data;
        final String hostname;

        ServerName(HandshakeInStream s) throws IOException {
            length = s.getInt16();      // ServerNameList length
            type = s.getInt8();         // NameType
            data = s.getBytes16();      // HostName (length read in getBytes16)
            if (type == NAME_HOST_NAME) {
                hostname = new String(data, "UTF8");
            } else {
                hostname = null;
            }
        }

        public String toString() {
            if (type == NAME_HOST_NAME) {
                return "host_name: " + hostname;
            } else {
                return "unknown-" + type + ": " + Debug.toString(data);
            }
        }
    }

    int length() {
        throw new RuntimeException("not yet supported");
    }

    void send(HandshakeOutStream s) throws IOException {
        throw new RuntimeException("not yet supported");
    }

    public String toString() {
        return "Unsupported extension " + type + ", " + names.toString();
    }
}

final class SupportedEllipticCurvesExtension extends HelloExtension {

    /* Class and subclass dynamic debugging support */
    private static final Debug debug = Debug.getInstance("ssl");

    private static final int ARBITRARY_PRIME = 0xff01;
    private static final int ARBITRARY_CHAR2 = 0xff02;

    // speed up the searching
    private static final Map<String, Integer> oidToIdMap = new HashMap<String, Integer>();
    private static final Map<Integer, String> idToOidMap = new HashMap<Integer, String>();

    // speed up the parameters construction
    private static final Map<Integer, AlgorithmParameters> idToParams =
            new HashMap<Integer, AlgorithmParameters>();

    // the supported elliptic curves
    private static final int[] supportedCurveIds;

    // the curves of the extension
    private final int[] curveIds;

    // See com.jn.langx.security.jsse.sun.security.util.CurveDB for the OIDs
    private static enum NamedEllipticCurve {
        T163_K1(1, "sect163k1", "1.3.132.0.1", true),  // NIST K-163
        T163_R1(2, "sect163r1", "1.3.132.0.2", false),
        T163_R2(3, "sect163r2", "1.3.132.0.15", true),  // NIST B-163
        T193_R1(4, "sect193r1", "1.3.132.0.24", false),
        T193_R2(5, "sect193r2", "1.3.132.0.25", false),
        T233_K1(6, "sect233k1", "1.3.132.0.26", true),  // NIST K-233
        T233_R1(7, "sect233r1", "1.3.132.0.27", true),  // NIST B-233
        T239_K1(8, "sect239k1", "1.3.132.0.3", false),
        T283_K1(9, "sect283k1", "1.3.132.0.16", true),  // NIST K-283
        T283_R1(10, "sect283r1", "1.3.132.0.17", true),  // NIST B-283
        T409_K1(11, "sect409k1", "1.3.132.0.36", true),  // NIST K-409
        T409_R1(12, "sect409r1", "1.3.132.0.37", true),  // NIST B-409
        T571_K1(13, "sect571k1", "1.3.132.0.38", true),  // NIST K-571
        T571_R1(14, "sect571r1", "1.3.132.0.39", true),  // NIST B-571

        P160_K1(15, "secp160k1", "1.3.132.0.9", false),
        P160_R1(16, "secp160r1", "1.3.132.0.8", false),
        P160_R2(17, "secp160r2", "1.3.132.0.30", false),
        P192_K1(18, "secp192k1", "1.3.132.0.31", false),
        P192_R1(19, "secp192r1", "1.2.840.10045.3.1.1", true), // NIST P-192
        P224_K1(20, "secp224k1", "1.3.132.0.32", false),
        P224_R1(21, "secp224r1", "1.3.132.0.33", true),  // NIST P-224
        P256_K1(22, "secp256k1", "1.3.132.0.10", false),
        P256_R1(23, "secp256r1", "1.2.840.10045.3.1.7", true), // NIST P-256
        P384_R1(24, "secp384r1", "1.3.132.0.34", true),  // NIST P-384
        P521_R1(25, "secp521r1", "1.3.132.0.35", true);  // NIST P-521

        int id;
        String name;
        String oid;
        boolean isFips;

        NamedEllipticCurve(int id, String name, String oid, boolean isFips) {
            this.id = id;
            this.name = name;
            this.oid = oid;
            this.isFips = isFips;

            if (oidToIdMap.put(oid, id) != null ||
                    idToOidMap.put(id, oid) != null) {

                throw new RuntimeException(
                        "Duplicate named elliptic curve definition: " + name);
            }
        }

        static NamedEllipticCurve getCurve(String name, boolean requireFips) {
            for (NamedEllipticCurve curve : NamedEllipticCurve.values()) {
                if (curve.name.equals(name) && (!requireFips || curve.isFips)) {
                    return curve;
                }
            }

            return null;
        }
    }

    static {
        boolean requireFips = SunJSSE.isFIPS();

        // hack code to initialize NamedEllipticCurve
        NamedEllipticCurve nec =
                NamedEllipticCurve.getCurve("secp256r1", false);

        // The value of the System Property defines a list of enabled named
        // curves in preference order, separated with comma.  For example:
        //
        //      jdk.tls.namedGroups="secp521r1, secp256r1, secp384r1"
        //
        // If the System Property is not defined or the value is empty, the
        // default curves and preferences will be used.
        String property = AccessController.doPrivileged(
                new GetPropertyAction("jdk.tls.namedGroups"));
        if (property != null && property.length() != 0) {
            // remove double quote marks from beginning/end of the property
            if (property.length() > 1 && property.charAt(0) == '"' &&
                    property.charAt(property.length() - 1) == '"') {
                property = property.substring(1, property.length() - 1);
            }
        }

        ArrayList<Integer> idList;
        if (property != null && property.length() != 0) {   // customized curves
            String[] curves = property.split(",");
            idList = new ArrayList<Integer>(curves.length);
            for (String curve : curves) {
                curve = curve.trim();
                if (!curve.isEmpty()) {
                    NamedEllipticCurve namedCurve =
                            NamedEllipticCurve.getCurve(curve, requireFips);
                    if (namedCurve != null) {
                        if (isAvailableCurve(namedCurve.id)) {
                            idList.add(namedCurve.id);
                        }
                    }   // ignore unknown curves
                }
            }
            if (idList.isEmpty() && JsseJce.isEcAvailable()) {
                throw new IllegalArgumentException(
                        "System property jdk.tls.namedGroups(" + property + ") " +
                                "contains no supported elliptic curves");
            }
        } else {        // default curves
            int[] ids;
            if (requireFips) {
                ids = new int[]{
                        // only NIST curves in FIPS mode
                        23, 24, 25, 9, 10, 11, 12, 13, 14,
                };
            } else {
                ids = new int[]{
                        // NIST curves first
                        23, 24, 25, 9, 10, 11, 12, 13, 14,
                        // non-NIST curves
                        22,
                };
            }

            idList = new ArrayList<Integer>(ids.length);
            for (int curveId : ids) {
                if (isAvailableCurve(curveId)) {
                    idList.add(curveId);
                }
            }
        }

        if (debug != null && idList.isEmpty()) {
            debug.println(
                    "Initialized [jdk.tls.namedGroups|default] list contains " +
                            "no available elliptic curves. " +
                            (property != null ? "(" + property + ")" : "[Default]"));
        }

        supportedCurveIds = new int[idList.size()];
        int i = 0;
        for (Integer id : idList) {
            supportedCurveIds[i++] = id;
        }
    }

    // check whether the curve is supported by the underlying providers
    private static boolean isAvailableCurve(int curveId) {
        String oid = idToOidMap.get(curveId);
        if (oid != null) {
            AlgorithmParameters params = null;
            try {
                params = JsseJce.getAlgorithmParameters("EC");
                params.init(new ECGenParameterSpec(oid));
            } catch (Exception e) {
                return false;
            }

            // cache the parameters
            idToParams.put(curveId, params);

            return true;
        }

        return false;
    }

    private SupportedEllipticCurvesExtension(int[] curveIds) {
        super(ExtensionType.EXT_ELLIPTIC_CURVES);
        this.curveIds = curveIds;
    }

    SupportedEllipticCurvesExtension(HandshakeInStream s, int len)
            throws IOException {
        super(ExtensionType.EXT_ELLIPTIC_CURVES);
        int k = s.getInt16();
        if (((len & 1) != 0) || (k + 2 != len)) {
            throw new SSLProtocolException("Invalid " + type + " extension");
        }

        // Note: unknown curves will be ignored later.
        curveIds = new int[k >> 1];
        for (int i = 0; i < curveIds.length; i++) {
            curveIds[i] = s.getInt16();
        }
    }

    // get the preferred active curve
    static int getActiveCurves(AlgorithmConstraints constraints) {
        return getPreferredCurve(supportedCurveIds, constraints);
    }

    static boolean hasActiveCurves(AlgorithmConstraints constraints) {
        return getActiveCurves(constraints) >= 0;
    }

    static SupportedEllipticCurvesExtension createExtension(
            AlgorithmConstraints constraints) {

        ArrayList<Integer> idList = new ArrayList<Integer>(supportedCurveIds.length);
        for (int curveId : supportedCurveIds) {
            if (constraints.permits(
                    EnumSet.of(CryptoPrimitive.KEY_AGREEMENT),
                    "EC", idToParams.get(curveId))) {
                idList.add(curveId);
            }
        }

        if (!idList.isEmpty()) {
            int[] ids = new int[idList.size()];
            int i = 0;
            for (Integer id : idList) {
                ids[i++] = id;
            }

            return new SupportedEllipticCurvesExtension(ids);
        }

        return null;
    }

    // get the preferred activated curve
    int getPreferredCurve(AlgorithmConstraints constraints) {
        return getPreferredCurve(curveIds, constraints);
    }

    // get a preferred activated curve
    private static int getPreferredCurve(int[] curves,
                                         AlgorithmConstraints constraints) {
        for (int curveId : curves) {
            if (isSupported(curveId) && constraints.permits(
                    EnumSet.of(CryptoPrimitive.KEY_AGREEMENT),
                    "EC", idToParams.get(curveId))) {
                return curveId;
            }
        }

        return -1;
    }

    boolean contains(int index) {
        for (int curveId : curveIds) {
            if (index == curveId) {
                return true;
            }
        }
        return false;
    }

    int length() {
        return 6 + (curveIds.length << 1);
    }

    void send(HandshakeOutStream s) throws IOException {
        s.putInt16(type.id);
        int k = curveIds.length << 1;
        s.putInt16(k + 2);
        s.putInt16(k);
        for (int curveId : curveIds) {
            s.putInt16(curveId);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Extension " + type + ", curve names: {");
        boolean first = true;
        for (int curveId : curveIds) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            String curveName = getCurveName(curveId);
            if (curveName != null) {
                sb.append(curveName);
            } else if (curveId == ARBITRARY_PRIME) {
                sb.append("arbitrary_explicit_prime_curves");
            } else if (curveId == ARBITRARY_CHAR2) {
                sb.append("arbitrary_explicit_char2_curves");
            } else {
                sb.append("unknown curve " + curveId);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Test whether the given curve is supported.
    static boolean isSupported(int index) {
        for (int curveId : supportedCurveIds) {
            if (index == curveId) {
                return true;
            }
        }

        return false;
    }

    static int getCurveIndex(ECParameterSpec params) {
        String oid = JsseJce.getNamedCurveOid(params);
        if (oid == null) {
            return -1;
        }
        Integer n = oidToIdMap.get(oid);
        return (n == null) ? -1 : n;
    }

    static String getCurveOid(int index) {
        return idToOidMap.get(index);
    }

    static ECGenParameterSpec getECGenParamSpec(int index) {
        AlgorithmParameters params = idToParams.get(index);
        try {
            return params.getParameterSpec(ECGenParameterSpec.class);
        } catch (InvalidParameterSpecException ipse) {
            // should be unlikely
            String curveOid = getCurveOid(index);
            return new ECGenParameterSpec(curveOid);
        }
    }

    private static String getCurveName(int index) {
        for (NamedEllipticCurve namedCurve : NamedEllipticCurve.values()) {
            if (namedCurve.id == index) {
                return namedCurve.name;
            }
        }

        return null;
    }
}

final class SupportedEllipticPointFormatsExtension extends HelloExtension {

    final static int FMT_UNCOMPRESSED = 0;
    final static int FMT_ANSIX962_COMPRESSED_PRIME = 1;
    final static int FMT_ANSIX962_COMPRESSED_CHAR2 = 2;

    static final HelloExtension DEFAULT =
            new SupportedEllipticPointFormatsExtension(new byte[]{FMT_UNCOMPRESSED});

    private final byte[] formats;

    private SupportedEllipticPointFormatsExtension(byte[] formats) {
        super(ExtensionType.EXT_EC_POINT_FORMATS);
        this.formats = formats;
    }

    SupportedEllipticPointFormatsExtension(HandshakeInStream s, int len)
            throws IOException {
        super(ExtensionType.EXT_EC_POINT_FORMATS);
        formats = s.getBytes8();
        // RFC 4492 says uncompressed points must always be supported.
        // Check just to make sure.
        boolean uncompressed = false;
        for (int format : formats) {
            if (format == FMT_UNCOMPRESSED) {
                uncompressed = true;
                break;
            }
        }
        if (uncompressed == false) {
            throw new SSLProtocolException
                    ("Peer does not support uncompressed points");
        }
    }

    int length() {
        return 5 + formats.length;
    }

    void send(HandshakeOutStream s) throws IOException {
        s.putInt16(type.id);
        s.putInt16(formats.length + 1);
        s.putBytes8(formats);
    }

    private static String toString(byte format) {
        int f = format & 0xff;
        switch (f) {
            case FMT_UNCOMPRESSED:
                return "uncompressed";
            case FMT_ANSIX962_COMPRESSED_PRIME:
                return "ansiX962_compressed_prime";
            case FMT_ANSIX962_COMPRESSED_CHAR2:
                return "ansiX962_compressed_char2";
            default:
                return "unknown-" + f;
        }
    }

    public String toString() {
        List<String> list = new ArrayList<String>();
        for (byte format : formats) {
            list.add(toString(format));
        }
        return "Extension " + type + ", formats: " + list;
    }
}

/*
 * For secure renegotiation, RFC5746 defines a new TLS extension,
 * "renegotiation_info" (with extension type 0xff01), which contains a
 * cryptographic binding to the enclosing TLS connection (if any) for
 * which the renegotiation is being performed.  The "extension data"
 * field of this extension contains a "RenegotiationInfo" structure:
 *
 *      struct {
 *          opaque renegotiated_connection<0..255>;
 *      } RenegotiationInfo;
 */
final class RenegotiationInfoExtension extends HelloExtension {
    private final byte[] renegotiated_connection;

    RenegotiationInfoExtension(byte[] clientVerifyData,
                               byte[] serverVerifyData) {
        super(ExtensionType.EXT_RENEGOTIATION_INFO);

        if (clientVerifyData.length != 0) {
            renegotiated_connection =
                    new byte[clientVerifyData.length + serverVerifyData.length];
            System.arraycopy(clientVerifyData, 0, renegotiated_connection,
                    0, clientVerifyData.length);

            if (serverVerifyData.length != 0) {
                System.arraycopy(serverVerifyData, 0, renegotiated_connection,
                        clientVerifyData.length, serverVerifyData.length);
            }
        } else {
            // ignore both the client and server verify data.
            renegotiated_connection = new byte[0];
        }
    }

    RenegotiationInfoExtension(HandshakeInStream s, int len)
            throws IOException {
        super(ExtensionType.EXT_RENEGOTIATION_INFO);

        // check the extension length
        if (len < 1) {
            throw new SSLProtocolException("Invalid " + type + " extension");
        }

        int renegoInfoDataLen = s.getInt8();
        if (renegoInfoDataLen + 1 != len) {  // + 1 = the byte we just read
            throw new SSLProtocolException("Invalid " + type + " extension");
        }

        renegotiated_connection = new byte[renegoInfoDataLen];
        if (renegoInfoDataLen != 0) {
            s.read(renegotiated_connection, 0, renegoInfoDataLen);
        }
    }


    // Length of the encoded extension, including the type and length fields
    int length() {
        return 5 + renegotiated_connection.length;
    }

    void send(HandshakeOutStream s) throws IOException {
        s.putInt16(type.id);
        s.putInt16(renegotiated_connection.length + 1);
        s.putBytes8(renegotiated_connection);
    }

    boolean isEmpty() {
        return renegotiated_connection.length == 0;
    }

    byte[] getRenegotiatedConnection() {
        return renegotiated_connection;
    }

    public String toString() {
        return "Extension " + type + ", renegotiated_connection: " +
                (renegotiated_connection.length == 0 ? "<empty>" :
                        Debug.toString(renegotiated_connection));
    }

}
