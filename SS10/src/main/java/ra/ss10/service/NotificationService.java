package ra.ss10.service;

import com.data.ss10.model.entity.Notification;
import ra.ss10.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {
    private NotificationRepository notificationRepository;
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    public List<Notification> getNotificationsByAccountId(UUID userId) {
        return notificationRepository.findByAccount_Id(userId);
    }
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
