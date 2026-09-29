package com.flowops.ticket.repository;

import com.flowops.ticket.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, UUID> {
    List<TicketHistory> findByTicketIdOrderByCreatedAtAsc(UUID ticketId);
}
