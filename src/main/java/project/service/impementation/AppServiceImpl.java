//package project.service.impementation;
//
//import org.springframework.stereotype.Service;
//import project.service.AppService;
//
//import java.util.Random;
//
//@Service
//public class AppServiceImpl implements AppService {
//
//    @Override
//    public String randomString(int length) {
//        char[] chars = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx1234567890".toCharArray();
//        Random random = new Random();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            stringBuilder.append(chars[random.nextInt(chars.length)]);
//        }
//        return stringBuilder.toString();
//    }
//
//}
