package workplaceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.UserManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

@Component
public class Security {

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private String keyStr = "pxTAoY3f";

    public String verifyUser(String token) {
        List<Users> userList = userManager.getUserList();

        for(Users user : userList) {
            String tokenForUser = getToken(user);
            if(token != null && token.equals(tokenForUser)) {
                return null;
            }
        }

        return "login";
    }

    public String getToken(Users user) {
        String password = decode(user.getPassword());
        String tokenNoCrypt = String.format("%s%s", user.getUsername(), password);
        return encode(tokenNoCrypt);
    }

    public String encode(String str) {
        try {
            return DatatypeConverter.printHexBinary(crypt(Cipher.ENCRYPT_MODE, str.getBytes()));
        } catch (Exception exception) {

        }

        return null;
    }

    private String decode(String str) {
        try {
            return new String(crypt(Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(str)));
        } catch (Exception exception) {

        }
        return null;
    }

    private byte[] crypt(int mode, byte[] valueByte) throws BadPaddingException {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            String algorithm = "AES";
            byte[] keyByte = sha.digest(keyStr.getBytes());
            keyByte = Arrays.copyOf(keyByte, 16);
            keyByte = Arrays.copyOf(keyByte, 16);
            SecretKeySpec secretKey = new SecretKeySpec(keyByte, algorithm);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(mode, secretKey);

            return cipher.doFinal(valueByte);
        } catch (BadPaddingException badPaddingException) {
            throw badPaddingException;
        } catch (Exception exception) {
            return null;
        }
    }
}
