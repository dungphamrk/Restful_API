package ra.ss14.controller;

import ra.ss14.model.dto.request.TicketRequestDTO;
import ra.ss14.model.dto.response.TicketResponseDTO;
import ra.ss14.security.principal.CustomUserDetails;
import ra.ss14.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/book")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketResponseDTO> bookTicket(
            @Valid @RequestBody TicketRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ticketService.bookTicket(dto, userDetails.getUsername()));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TicketResponseDTO>> myTickets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ticketService.getMyTickets(userDetails.getUsername()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
}