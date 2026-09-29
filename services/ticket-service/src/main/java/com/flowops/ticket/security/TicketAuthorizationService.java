package com.flowops.ticket.security;

import com.flowops.ticket.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketAuthorizationService {
    private final CurrentUser currentUser;

    public boolean canView(Ticket ticket) {

        UUID userId =
                currentUser.getUserId();

        if (currentUser.isAdmin()) {
            return true;
        }

        if (currentUser.isManager()) {
            return true;
        }

        if (currentUser.isItAgent()
                && userId.equals(ticket.getAssigneeId())) {

            return true;
        }

        return userId.equals(
                ticket.getReporterId()
        );
    }

    public boolean canModify(Ticket ticket) {

        UUID userId =
                currentUser.getUserId();

        if (currentUser.isAdmin()) {
            return true;
        }

        return currentUser.isItAgent()
                && userId.equals(
                ticket.getAssigneeId()
        );
    }
}
