package workplaceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.UserManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class Security {

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    private String keyStr;// = "pxTAoY3f";

    public Security() {
        keyStr = generateString();
    }

    public ModelAndView verifyUser(String token, String page) {
        ModelAndView modelAndView = new ModelAndView("login");

        List<Users> userList = userManager.getUserList();

        for (Users user : userList) {
            String tokenForUser = getToken(user);
            System.out.println("token=" + token);
            System.out.println("tokenForUser=" + tokenForUser);
            if (token != null && token.equals(tokenForUser) && user.getRole() == Role.ADMIN) {
                modelAndView.setViewName(page);
                modelAndView.addObject("token", token);
                //modelAndView.getModelMap().addAttribute("token", token);
                return modelAndView;
            }
        }

        return modelAndView;
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

    public static void generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            int keyBitSize = 256;

            keyGenerator.init(keyBitSize, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();


            System.out.println(secretKey);

        } catch (Exception exception) {

        }
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "uuid = " + uuid;
    }

    public static void main(String[] args) {
        Security security = new Security();

        String login="asd";
        String encode = security.encode(login);
        System.out.println(encode);
        String decode = security.decode(encode);
        System.out.println(decode);

        security = new Security();

        encode = security.encode(login);
        System.out.println(encode);
        decode = security.decode(encode);
        System.out.println(decode);
    }
}
