package com.poscoict.posledger.assets.chaincode;

import com.poscoict.posledger.assets.config.SetConfig;
import com.poscoict.posledger.assets.user.UserContext;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.io.pem.PemReader;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AddressUtils {



    public static boolean isValidAddress(String address) {
        if (address == null || address.length() != 42) {
            return false;
        }
        String checksum = checksum(address.replace("0x", "").toLowerCase());
        return checksum.equals(address);
    }

    static String getAddressFor(Certificate certificate) {
        return getAddressFor(certificate.getPublicKey().getEncoded());
    }

    static String getAddressFor(byte[] pubKey) {
        String pubKeyHash = sha3String(pubKey);
        String shortPubKeyHash = pubKeyHash.substring(pubKeyHash.length() - 40); // keep last 20 bytes
        return checksum(shortPubKeyHash);
    }

    private static byte[] sha3(byte[] input) {
        Keccak.Digest256 kecc = new Keccak.Digest256();
        kecc.update(input, 0, input.length);
        return kecc.digest();
    }

    private static String sha3String(String input) {
        return sha3String(input.getBytes(UTF_8));
    }

    private static String sha3String(byte[] input) {
        return toHex(sha3(input));
    }

    private static String toHex(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : input) {
            stringBuilder.append(String.format("%02x", b & 0xFF));
        }
        return stringBuilder.toString();
    }

    private static String checksum(String lowercaseHashWithoutPrefix) {
        String inputHash = sha3String(lowercaseHashWithoutPrefix);
        StringBuilder result = new StringBuilder();
        result.append("0x");

        for (int i = 0; i < lowercaseHashWithoutPrefix.length(); i++) {
            if (Integer.parseInt(String.valueOf(inputHash.charAt(i)), 16) >= 8) {
                result.append(String.valueOf(lowercaseHashWithoutPrefix.charAt(i)).toUpperCase());
            } else {
                result.append(lowercaseHashWithoutPrefix.charAt(i));
            }
        }

        return result.toString();
    }

    private String getAddressOf(byte[] publicKey) {
        return AddressUtils.getAddressFor(publicKey);
    }

    public static String getMyAddress(String userId, Enrollment enrollment) {

        UserContext userContext = SetConfig.initUserContext(userId, enrollment);
//        UserContext userContext = new UserContext();
//        userContext.setName(userId);
//        userContext.setAffiliation("org1.department1");
//        userContext.setMspId("Org1MSP");
//        userContext.setEnrollment(enrollment);
        X509Identity identity = new X509Identity(userContext);

        //AddressUtils addressUtils = new AddressUtils();
        //String addr = addressUtils.getMyAddress(identity);
        //System.out.println(addr);
        return AddressUtils.getAddressFor(getMyCertificate(identity));
    }

//    public static String getMyAddress(X509Identity _identity) {
//        return AddressUtils.getAddressFor(getMyCertificate(_identity));
//    }

    public static X509Certificate getMyCertificate(X509Identity _identity) {
        try {
            Identities.SerializedIdentity identity = _identity.createSerializedIdentity();
            StringReader reader = new StringReader(identity.getIdBytes().toStringUtf8());
            PemReader pr = new PemReader(reader);
            byte[] x509Data = pr.readPemObject().getContent();
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(x509Data));
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }
}