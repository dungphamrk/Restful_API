package ra.ss2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss2.entity.ScreenRoom;

import java.util.List;

public interface ScreenRoomRepository  extends JpaRepository<ScreenRoom, Long> {
    List<ScreenRoom> findByNameContainingIgnoreCase(String name);
    List<ScreenRoom> findByTheaterId(Long theaterId);
}
