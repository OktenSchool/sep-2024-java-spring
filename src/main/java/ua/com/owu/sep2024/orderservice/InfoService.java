package ua.com.owu.sep2024.orderservice;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfoService {

    @Value("${app.info.address:NO ADDRESS}")
    private String address;

    @Value("${app.info.phone}")
    private String phone;

    public Info getInfo() {
        return Info.builder()
                .address(address)
                .phone(phone).build();
    }
}
