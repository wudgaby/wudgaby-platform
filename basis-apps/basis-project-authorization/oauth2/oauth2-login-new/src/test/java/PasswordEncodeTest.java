import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/9/20 4:15
 * @Desc :
 */
public class PasswordEncodeTest {
    public static void main(String[] args) {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("aa0000"));
    }
}
