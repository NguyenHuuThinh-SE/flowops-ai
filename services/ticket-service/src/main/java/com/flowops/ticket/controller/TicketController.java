package com.flowops.ticket.controller;

import com.flowops.ticket.Service.TicketService;
import com.flowops.ticket.dto.request.AssignTicketRequest;
import com.flowops.ticket.dto.request.CreateTicketRequest;
import com.flowops.ticket.dto.response.PageResponse;
import com.flowops.ticket.dto.response.TicketResponse;
import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.enums.TicketStatus;
import com.flowops.ticket.repository.TicketRepository;
import com.flowops.ticket.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("""
               hasAnyRole('EMPLOYEE',
                           'IT_AGENT',
                           'MANAGER',
                           'ADMIN')
            """)
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request) {
        // Tạm thời lấy UUID test.
        // Sau khi kết nối JWT Security sẽ lấy từ SecurityContext.

        return ticketService.createTicket(request, currentUser.getUserId());
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'ADMIN')
            """)
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @Valid @RequestBody AssignTicketRequest request
    ) {

        return ticketService.assignTicket(
                id,
                request.getAssigneeId(),
                currentUser.getUserId()
        );
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'ADMIN')
            """)
    public TicketResponse startTicket(
            @PathVariable UUID id
    ) {

        return ticketService.startTicket(
                id,
                currentUser.getUserId()
        );
    }

    @PostMapping("/{id}/wait")
    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'ADMIN')
            """)
    public TicketResponse waitTicket(
            @PathVariable UUID id
    ) {

        return ticketService.waitTicket(
                id,
                currentUser.getUserId()
        );
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'ADMIN')
            """)
    public TicketResponse resolveTicket(
            @PathVariable UUID id
    ) throws AccessDeniedException {

        return ticketService.resolveTicket(
                id,
                currentUser.getUserId()
        );
    }


    @PostMapping("/{id}/close")
    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'MANAGER', 'ADMIN')
            """)
    public TicketResponse closeTicket(
            @PathVariable UUID id
    ) {

        return ticketService.closeTicket(
                id,
                currentUser.getUserId()
        );
    }

    @PreAuthorize("""
                hasAnyRole('IT_AGENT', 'MANAGER', 'ADMIN')
            """)
    @PostMapping("/{id}/reopen")
    public TicketResponse reopenTicket(
            @PathVariable UUID id
    ) {

        return ticketService.reopenTicket(
                id,
                currentUser.getUserId()
        );
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable UUID id) throws AccessDeniedException {
        return ticketService.getTickets(id);
    }

    @GetMapping
    public PageResponse<TicketResponse> getTickets(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,

            @RequestParam(required = false)
            TicketStatus status
    ) {
        if (size > 100) {
            size = 100;
        }

        return ticketService.getTickets(
                page,
                size,
                status
        );
    }

//    @GetMapping
//    public List<Ticket> getTickets() {
//        return ticketRepository.findAll();
//    }
}
