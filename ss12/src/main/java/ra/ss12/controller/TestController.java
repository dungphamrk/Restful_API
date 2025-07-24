package ra.ss12.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/private")
    public String privateArea() {
        return "Bạn đã đăng nhập thành công!";
    }

    @GetMapping("/public/info")
    public String publicInfo() {
        return "Thông tin công khai!";
    }
}