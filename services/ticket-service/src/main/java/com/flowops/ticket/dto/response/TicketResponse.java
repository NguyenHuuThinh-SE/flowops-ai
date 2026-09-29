package com.flowops.ticket.dto.response;

import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.enums.TicketCategory;
import com.flowops.ticket.enums.TicketPriority;
import com.flowops.ticket.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String ticketNumber,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        TicketCategory category,
        UUID reporterId,
        UUID assigneeId,
        String department,
        LocalDateTime slaDeadline,
        LocalDateTime resolvedAt,
        LocalDateTime closedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TicketResponse from(Ticket ticket) {

        return new TicketResponse(
                ticket.getId(),
                ticket.getTicketNumber(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCategory(),
                ticket.getReporterId(),
                ticket.getAssigneeId(),
                ticket.getDepartment(),
                ticket.getSlaDeadline(),
                ticket.getResolvedAt(),
                ticket.getClosedAt(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
