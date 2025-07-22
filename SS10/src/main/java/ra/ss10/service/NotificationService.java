package ra.ss10.service;

import ra.ss10.entity.model.Notification;
import ra.ss10.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
        log.info("NotificationService initialized");
    }

    public List<Notification> getNotificationsByAccountId(UUID userId) {
        try {
            log.info("Retrieving notifications for account ID: {}", userId);
            List<Notification> notifications = notificationRepository.findByAccount_Id(userId);
            if (notifications.isEmpty()) {
                log.warn("No notifications found for account ID: {}", userId);
            } else {
                log.info("Successfully retrieved {} notifications for account ID: {}", notifications.size(), userId);
            }
            return notifications;
        } catch (Exception e) {
            log.error("Failed to retrieve notifications for account ID: {}. Error: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to retrieve notifications", e);
        }
    }

    public Notification save(Notification notification) {
        try {
            log.info("Saving notification for account ID: {}", notification.getAccount().getId());
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Successfully saved notification with ID: {}", savedNotification.getId());
            return savedNotification;
        } catch (Exception e) {
            log.error("Failed to save notification for account ID: {}. Error: {}",
                    notification.getAccount().getId(), e.getMessage());
            throw new RuntimeException("Failed to save notification", e);
        }
    }
}