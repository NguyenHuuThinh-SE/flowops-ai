package com.flowops.ticket.controller;

import com.flowops.ticket.Service.TicketHistoryService;
import com.flowops.ticket.entity.TicketHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets/{ticketId}/history")
public class TicketHistoryController {
    private final TicketHistoryService historyService;

    @GetMapping
    public List<TicketHistory> getHistory(@PathVariable UUID ticketId) {
        return historyService.getHistory(ticketId);
    }
}
