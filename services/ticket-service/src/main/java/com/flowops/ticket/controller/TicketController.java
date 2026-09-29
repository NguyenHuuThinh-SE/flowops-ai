package com.flowops.ticket.controller;

import com.flowops.ticket.Service.TicketService;
import com.flowops.ticket.dto.request.AssignTicketRequest;
import com.flowops.ticket.dto.request.CreateTicketRequest;
import com.flowops.ticket.dto.response.PageResponse;
import com.flowops.ticket.dto.response.TicketResponse;
import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.enums.TicketStatus;
import com.flowops.ticket.repository.TicketRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request) {
        // Tạm thời lấy UUID test.
        // Sau khi kết nối JWT Security sẽ lấy từ SecurityContext.
        UUID reporterId = UUID.randomUUID();

        return ticketService.createTicket(request, reporterId);
    }

    @PostMapping("/{id}/assign")
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @Valid @RequestBody AssignTicketRequest request
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.assignTicket(
                id,
                request.getAssigneeId(),
                actorId
        );
    }

    @PostMapping("/{id}/start")
    public TicketResponse startTicket(
            @PathVariable UUID id
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.startTicket(
                id,
                actorId
        );
    }

    @PostMapping("/{id}/wait")
    public TicketResponse waitTicket(
            @PathVariable UUID id
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.waitTicket(
                id,
                actorId
        );
    }

    @PostMapping("/{id}/resolve")
    public TicketResponse resolveTicket(
            @PathVariable UUID id
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.resolveTicket(
                id,
                actorId
        );
    }


    @PostMapping("/{id}/close")
    public TicketResponse closeTicket(
            @PathVariable UUID id
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.closeTicket(
                id,
                actorId
        );
    }

    @PostMapping("/{id}/reopen")
    public TicketResponse reopenTicket(
            @PathVariable UUID id
    ) {

        UUID actorId = UUID.randomUUID();

        return ticketService.reopenTicket(
                id,
                actorId
        );
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable UUID id) {
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
