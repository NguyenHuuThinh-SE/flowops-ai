package com.flowops.ticket.controller;

import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }
}
