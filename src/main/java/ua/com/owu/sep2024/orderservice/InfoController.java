package ua.com.owu.sep2024.orderservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final InfoService infoService;

    private final Info info;

    public InfoController(InfoService infoService, @Qualifier("defaultInfo") Info info) {
        this.infoService = infoService;
        this.info = info;
    }

    @GetMapping("/info")
    public Info getInfo() {
        return info;
    }
}
