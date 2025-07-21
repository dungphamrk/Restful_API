package ra.ss9.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo-log")
public class DemoLogController {

    private static final Logger logger = LoggerFactory.getLogger(DemoLogController.class);

    @GetMapping("/trace")
    public String trace() {
        logger.trace("Đã ghi log trace");
        return "Trace log ghi thành công";
    }

    @GetMapping("/debug")
    public String debug() {
        logger.debug("Đã ghi log debug");
        return "Debug log ghi thành công";
    }

    @GetMapping("/info")
    public String info() {
        logger.info("Đã ghi log info");
        return "Info log ghi thành công";
    }

    @GetMapping("/warning")
    public String warning() {
        logger.warn("Đã ghi log warning");
        return "Warning log ghi thành công";
    }

    @GetMapping("/error")
    public String error() {
        logger.error("Đã ghi log error");
        return "Error log ghi thành công";
    }
}
