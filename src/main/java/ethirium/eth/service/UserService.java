package ethirium.eth.service;

import ethirium.eth.dto.UserDto;
import ethirium.eth.model.User;
import ethirium.eth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createUserIfNotExist() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        User user =userRepo.findByUserID(1);
        if(user!=null){
            return;
        }

        user=new User();
        user.setPassword(getEncryptedPassword("password"));
        user.setUserID(1);
        user.setUserName("admin");

        userRepo.save(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isUserCorrect(UserDto userDto) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String pass=getEncryptedPassword(userDto.getPassword());

        User user=userRepo.findByUserID(1);

        return user.getUserName().equals(userDto.getUserName()) && pass.equals(user.getPassword());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateUser(UserDto userDto) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        boolean isUserCorrect=isUserCorrect(userDto);
        if(isUserCorrect) {
            User user = userRepo.findByUserID(1);
            user.setPassword(getEncryptedPassword(userDto.getNewPassword()));
            return true;
        }
        return false;
    }


    private String getEncryptedPassword(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String key = "INVO2345INr12345"; // 128 bit key
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return new String(encrypted);
    }

    public String getDecryptedPassword(String cipherPassword) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        String key = "INVO2345INr12345"; // 128 bit key
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(cipherPassword.getBytes()));

        return decrypted;

    }

    @PostConstruct
    public void createNewUser(){
        try {
            this.createUserIfNotExist();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
