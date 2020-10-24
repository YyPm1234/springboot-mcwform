package zone.mcw.mcwzone.springbootmcwform.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 加密utils
 *
 * @author by W4i
 * @date 2020/9/22 20:51
 */
public class EncryptionUtils {
	private final static String seedCode = "mmmmmmmm";

	/**
	 * md5加密
	 *
	 * @param str
	 * @return
	 */
	public String getMD5String(String str) {
		try {
			str = str + "mmmmmmcw";
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			//一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 6位验证码
	 *
	 * @return
	 */
	public String getCode() {
		String code = "";
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			int ran1 = r.nextInt(10);
			if (ran1 > 4) {
				ran1 = r.nextInt(10);
				code = ran1 + code;
			} else {
				char c = (char) (int) (Math.random() * 26 + 97);
				String b = String.valueOf(c).toUpperCase();
				code = b + code;
			}
		}
		return code;
	}

	/**
	 * 对称加密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String encode(String str) throws Exception {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(seedCode.getBytes());
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] byteContent = str.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(byteContent);
		return Base64.encodeBase64String(result);

	}

	/**
	 * 对称解密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String decode(String str) throws Exception {
		byte[] content = Base64.decodeBase64(str);
		//防止linux下 随机生成key
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(seedCode.getBytes());
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return new String(result);
	}

}