package com.flowops.ticket.Service;

import com.flowops.ticket.dto.request.CreateTicketRequest;
import com.flowops.ticket.dto.response.PageResponse;
import com.flowops.ticket.dto.response.TicketResponse;
import com.flowops.ticket.entity.Ticket;
import com.flowops.ticket.entity.TicketHistory;
import com.flowops.ticket.enums.TicketAction;
import com.flowops.ticket.enums.TicketPriority;
import com.flowops.ticket.enums.TicketStatus;
import com.flowops.ticket.exception.InvalidTicketStateException;
import com.flowops.ticket.exception.ResourceNotFoundException;
import com.flowops.ticket.repository.TicketHistoryRepository;
import com.flowops.ticket.repository.TicketRepository;
import com.flowops.ticket.security.TicketAuthorizationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository historyRepository;
    private final TicketAuthorizationService authorizationService;

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request, UUID reporterId) {
        Ticket ticket = new Ticket();

        ticket.setTicketNumber(generateTicketNumber());
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setDepartment(request.getDepartment());

        ticket.setReporterId(reporterId);
        ticket.setStatus(TicketStatus.OPEN);

        ticket.setSlaDeadline(calculateSlaDeadline(request.getPriority()));
        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                reporterId,
                TicketAction.CREATED,
                null,
                TicketStatus.OPEN,
                "Ticket created");

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse assignTicket(UUID ticketId, UUID assigneeId, UUID actorId) {
        Ticket ticket = getTicketById(ticketId);

        UUID oldAssignee = ticket.getAssigneeId();

        ticket.setAssigneeId(assigneeId);

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.ASSIGNED,
                saved.getStatus(),
                saved.getStatus(),
                "Assigned from "
                        + oldAssignee
                        + " to "
                        + assigneeId
        );

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse startTicket(
            UUID ticketId,
            UUID actorId
    ) {

        Ticket ticket = getTicketById(ticketId);

        validateTransition(
                ticket.getStatus(),
                TicketStatus.IN_PROGRESS
        );

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.STARTED,
                oldStatus,
                TicketStatus.IN_PROGRESS,
                "Ticket started"
        );

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse waitTicket(
            UUID ticketId,
            UUID actorId
    ) {

        Ticket ticket = getTicketById(ticketId);

        validateTransition(
                ticket.getStatus(),
                TicketStatus.WAITING
        );

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.WAITING);

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.WAITING,
                oldStatus,
                TicketStatus.WAITING,
                "Ticket is waiting"
        );

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse resolveTicket(
            UUID ticketId,
            UUID actorId
    ) throws AccessDeniedException {

        Ticket ticket = getTicketById(ticketId);

        if (!authorizationService.canModify(ticket))
            throw new AccessDeniedException("You are not assigned to this ticket");


        validateTransition(
                ticket.getStatus(),
                TicketStatus.RESOLVED
        );

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.RESOLVED,
                oldStatus,
                TicketStatus.RESOLVED,
                "Ticket resolved"
        );

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse closeTicket(
            UUID ticketId,
            UUID actorId
    ) {

        Ticket ticket = getTicketById(ticketId);

        validateTransition(
                ticket.getStatus(),
                TicketStatus.CLOSED
        );

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setClosedAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.CLOSED,
                oldStatus,
                TicketStatus.CLOSED,
                "Ticket closed"
        );

        return TicketResponse.from(saved);
    }

    @Transactional
    public TicketResponse reopenTicket(
            UUID ticketId,
            UUID actorId
    ) {

        Ticket ticket = getTicketById(ticketId);

        validateTransition(
                ticket.getStatus(),
                TicketStatus.IN_PROGRESS
        );

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setResolvedAt(null);
        ticket.setClosedAt(null);

        Ticket saved = ticketRepository.save(ticket);

        createHistory(
                saved,
                actorId,
                TicketAction.REOPENED,
                oldStatus,
                TicketStatus.IN_PROGRESS,
                "Ticket reopened"
        );

        return TicketResponse.from(saved);
    }

    // Lấy danh sách Ticket + pagination
    public PageResponse<TicketResponse> getTickets(int page, int size, TicketStatus status) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Ticket> ticketPage;
        if (status != null) {
            ticketPage = ticketRepository.findByStatus(
                    status,
                    pageable
            );
        } else {
            ticketPage = ticketRepository.findAll(
                    pageable
            );
        }

        return new PageResponse<>(
                ticketPage.getContent()
                        .stream()
                        .map(TicketResponse::from)
                        .toList(),

                ticketPage.getNumber(),
                ticketPage.getSize(),
                ticketPage.getTotalElements(),
                ticketPage.getTotalPages(),
                ticketPage.isFirst(),
                ticketPage.isLast()
        );
    }

    // Lấy 1 Ticket
    public TicketResponse getTickets(UUID ticketId) throws AccessDeniedException {
        Ticket ticket = getTicketById(ticketId);

        if (!authorizationService.canView(ticket)) {
            throw new AccessDeniedException("You do not have permission to view this ticket");
        }
        return TicketResponse.from(ticket);
    }


    private void validateTransition(TicketStatus from, TicketStatus to) {
        boolean valid = switch (from) {
            case OPEN -> to == TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> to == TicketStatus.WAITING || to == TicketStatus.RESOLVED;
            case WAITING -> to == TicketStatus.IN_PROGRESS;
            case RESOLVED -> to == TicketStatus.CLOSED || to == TicketStatus.IN_PROGRESS;
            case CLOSED -> false;
        };
        if (!valid) {
            throw new InvalidTicketStateException(
                    "Invalid ticket transition: "
                            + from
                            + " -> "
                            + to
            );
        }
    }


    private Ticket getTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
    }

    private void createHistory(
            Ticket ticket,
            UUID actorId,
            TicketAction action,
            TicketStatus from,
            TicketStatus to,
            String details
    ) {
        TicketHistory history = new TicketHistory();

        history.setTicketId(ticket.getId());
        history.setActorId(actorId);
        history.setAction(action);
        history.setFromStatus(from);
        history.setToStatus(to);
        history.setDetails(details);

        historyRepository.save(history);
    }

    private String generateTicketNumber() {
        return "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private LocalDateTime calculateSlaDeadline(TicketPriority priority) {
        LocalDateTime now = LocalDateTime.now();
        return switch (priority) {
            case LOW -> now.plusHours(72);
            case MEDIUM -> now.plusHours(48);
            case HIGH -> now.plusHours(24);
            case CRITICAL -> now.plusHours(4);
        };
    }
}
