import cn.hutool.core.io.resource.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/19 0019 11:44
 * @desc :
 */
public class KeyStoreTest {
    public static void main(String[] args) throws Exception {
        exportKeysAndCertsFromKeyStore();
    }

    /**
     * 导出证书、公钥、私钥
     * */
    public static void exportKeysAndCertsFromKeyStore() throws Exception {
        //以 PKCS12 规格，创建 KeyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //载入 jks 和该 jks 的密码 到 KeyStore 内
        keyStore.load(Files.newInputStream(new ClassPathResource("hw.keystore").getFile().toPath()), "112233".toCharArray());

        String path = new ClassPathResource("hw.keystore").getFile().getParent();

        // 要获取 key，需要提供 KeyStore 的别名 和该 KeyStore 的密码
        // 获取 keyStore 内所有别名 alias
        Enumeration<String> aliases = keyStore.aliases();

        //文档写入格式换行+Base64
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        final Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.out.println("jks文件别名是：" + alias);
            char[] keyPassword = "112233".toCharArray();

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword);
            System.out.println("jks文件中的私钥是：\nkey format: " + privateKey.getFormat() + "\n" + "-----BEGIN PRIVATE KEY-----" + LINE_SEPARATOR + new String(encoder.encode(privateKey.getEncoded())) + LINE_SEPARATOR + "-----END PRIVATE KEY-----" + "\n");
            String keyContent = "-----BEGIN PRIVATE KEY-----" + LINE_SEPARATOR + new String(encoder.encode(privateKey.getEncoded())) + LINE_SEPARATOR + "-----END PRIVATE KEY-----";
            writeKeyOrCertToFile(path + File.separator + alias + ".key", keyContent);

            Certificate certificate = keyStore.getCertificate(alias);
            X509Certificate cert = (X509Certificate) certificate;

            System.out.println("jks文件中的证书是：\ncertificate format: " + certificate.getType() + "\n" + "-----BEGIN CERTIFICATE-----" + LINE_SEPARATOR + new String(encoder.encode(certificate.getEncoded())) + LINE_SEPARATOR + "-----END CERTIFICATE-----");
            String certificateContent = "-----BEGIN CERTIFICATE-----" + LINE_SEPARATOR + new String(encoder.encode(certificate.getEncoded())) + LINE_SEPARATOR + "-----END CERTIFICATE-----";
            writeKeyOrCertToFile(path +File.separator +alias +".cert", certificateContent);

            PublicKey publicKey = certificate.getPublicKey();
            System.out.println("jks文件中的公钥是：\npublic key format: " + publicKey.getFormat() + "\n" + "-----BEGIN PUBLIC KEY-----" + LINE_SEPARATOR + new String(encoder.encode(publicKey.getEncoded())) + LINE_SEPARATOR + "-----END PUBLIC KEY-----");
            String cerContent = "-----BEGIN PUBLIC KEY-----" + LINE_SEPARATOR + new String(encoder.encode(publicKey.getEncoded())) + LINE_SEPARATOR + "-----END PUBLIC KEY-----";
            writeKeyOrCertToFile(path +File.separator +alias +".pub", cerContent);

            String msg = "你好";
            byte[] singBytes = sign(msg, privateKey);
            System.out.println(verify(msg, singBytes, publicKey));
        }
    }

    public static void writeKeyOrCertToFile(String filePathAndName, String fileContent) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePathAndName);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(fileContent);
        bw.close();
    }

    //私钥签名
    public static byte[] sign(String content, PrivateKey priKey) throws Exception {
        //这里可以从证书中解析出签名算法名称
        //Signature signature = Signature.getInstance(cert.getAlgorithm());
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(priKey);
        signature.update(content.getBytes());
        return signature.sign();
    }
    //公钥验签
    public static boolean verify(String content, byte[] sign, PublicKey pubKey) throws Exception {
        //这里可以从证书中解析出签名算法名称
        //Signature signature = Signature.getInstance(cert.getAlgorithm());
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(pubKey);
        signature.update(content.getBytes());
        return signature.verify(sign);
    }


}
