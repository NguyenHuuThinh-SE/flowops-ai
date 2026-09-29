package com.flowops.ticket.repository;

import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findByReporterId(UUID reporterId, Pageable pageable);

    Page<Ticket> findByAssigneeId(UUID assigneeId, Pageable pageable);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByReporterIdAndStatus(UUID reporterId, TicketStatus status, Pageable pageable);

    Page<Ticket> findByAssigneeIdAndStatus(UUID assigneeId, TicketStatus status, Pageable pageable);
}