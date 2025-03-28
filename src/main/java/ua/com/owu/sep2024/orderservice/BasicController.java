package ua.com.owu.sep2024.orderservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller + @ResponseBody
public class BasicController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello/{name}")
    public String helloFrom(@PathVariable("name") String name) {
        return "Hello " + name;
    }

    @GetMapping("/hello-to")
    public String helloTo(@RequestParam String name, @RequestParam(name = "age", required = false) Integer personAge) {
        if (personAge == null) {
            return "Hello " + name;
        } else {
            return "Hello " + name + " (" + personAge + " years old)";
        }
    }
}
