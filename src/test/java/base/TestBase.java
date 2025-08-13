package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import clients.AuthClient;
import models.auth.UserRegisterRequest;
import models.auth.UserRegisterResponse;
import utils.DataHelper;

public class TestBase {
    protected DataHelper dataHelper;
    protected AuthClient authClient;
    protected UserRegisterRequest user;
    protected UserRegisterResponse registeredUser;

    @BeforeEach
    public void setUp() {
        this.dataHelper = new DataHelper();
        this.authClient = new AuthClient();
        this.user = this.dataHelper.createRandomUser();
    }

    @AfterEach
    public void tearDown() {
        try {
            if (this.registeredUser != null) { // этим проверяем, что зареганный юзер таки есть
                String token = this.registeredUser.getAccessToken(); // обработать в условии?
                this.authClient.deleteUser(token);
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя: " + e);
        }
    }
}
