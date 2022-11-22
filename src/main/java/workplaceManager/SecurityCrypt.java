package workplaceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import workplaceManager.db.domain.Role;
import workplaceManager.db.domain.Users;
import workplaceManager.db.service.UserManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class SecurityCrypt {

    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public ModelAndView verifyUser(String token, String page) {
        ModelAndView modelAndView = new ModelAndView(Pages.login);

        System.out.println("page=" + page);

        List<Users> userList = userManager.getUserList();

        for (Users user : userList) {
            String tokenForUser = getToken(user);
            if (token != null && token.equals(tokenForUser)) {
                String passNoCrypt = decode(user.getPassword(), user.getSalt());
                user.setSalt(generateKey());
                user.setPassword(encode(passNoCrypt, user.getSalt()));
                userManager.save(user);

                String tokenNew = getToken(user);

                modelAndView.setViewName(page);
                modelAndView.addObject("token", tokenNew);
                modelAndView.addObject("role", user.getRole());
                return modelAndView;
            }
        }

        return modelAndView;
    }

    public String getToken(Users user) {
        String password = decode(user.getPassword(), user.getSalt());
        String tokenNoCrypt = String.format("%s%s", user.getUsername(), password);
        return encode(tokenNoCrypt, user.getSalt());
    }

    public String encode(String str, String key) {
        try {
            return DatatypeConverter.printHexBinary(crypt(Cipher.ENCRYPT_MODE, str.getBytes(), key));
        } catch (Exception exception) {

        }

        return null;
    }

    private String decode(String str, String key) {
        try {
            return new String(crypt(Cipher.DECRYPT_MODE, DatatypeConverter.parseHexBinary(str), key));
        } catch (Exception exception) {

        }
        return null;
    }

    private byte[] crypt(int mode, byte[] valueByte, String keyStr) throws BadPaddingException {
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

    public String generateKey() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
