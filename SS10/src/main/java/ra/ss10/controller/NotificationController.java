package ra.ss10.controller;

import com.data.ss10.model.entity.Notification;
import com.data.ss10.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Notification>> getNotificationsByAccountId(@PathVariable UUID accountId) {
        List<Notification> notifications = notificationService.getNotificationsByAccountId(accountId);
        return ResponseEntity.ok(notifications);
    }
}
