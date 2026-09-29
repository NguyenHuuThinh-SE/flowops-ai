package com.flowops.ticket.Service;

import com.flowops.ticket.entity.TicketHistory;
import com.flowops.ticket.exception.ResourceNotFoundException;
import com.flowops.ticket.repository.TicketHistoryRepository;
import com.flowops.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketHistoryService {
    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository historyRepository;

    public List<TicketHistory> getHistory(
            UUID ticketId
    ) {

        ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found: " + ticketId
                        )
                );

        return historyRepository
                .findByTicketIdOrderByCreatedAtAsc(ticketId);
    }
}
