package ra.ss14.service.impl;

import ra.ss14.model.dto.request.TicketRequestDTO;
import ra.ss14.model.dto.response.TicketResponseDTO;
import ra.ss14.model.entity.ShowTime;
import ra.ss14.model.entity.Ticket;
import ra.ss14.model.entity.User;
import ra.ss14.repository.ShowTimeRepo;
import ra.ss14.repository.TicketRepo;
import ra.ss14.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.ss14.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;
    private final UserRepo userRepo;
    private final ShowTimeRepo showtimeRepo;

    @Override
    public TicketResponseDTO bookTicket(TicketRequestDTO dto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShowTime showtime = showtimeRepo.findById(dto.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        Ticket ticket = Ticket.builder()
                .user(user)
                .showTime(showtime)
                .seatNumber(dto.getSeatNumber())
                .price(dto.getPrice())
                .bookingTime(LocalDateTime.now())
                .build();

        ticketRepo.save(ticket);

        return mapToDTO(ticket);
    }

    @Override
    public List<TicketResponseDTO> getMyTickets(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepo.findByUser(user).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TicketResponseDTO mapToDTO(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .seatNumber(ticket.getSeatNumber())
                .price(ticket.getPrice())
                .bookingTime(ticket.getBookingTime())
                .showtimeId(ticket.getShowTime().getId())
                .username(ticket.getUser().getUsername())
                .build();
    }
}
