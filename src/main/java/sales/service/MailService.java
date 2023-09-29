package sales.service;

public interface MailService {
    void sendCode(int code, String name, String username);
}
