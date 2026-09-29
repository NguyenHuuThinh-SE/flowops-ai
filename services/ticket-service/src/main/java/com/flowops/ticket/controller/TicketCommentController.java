package com.flowops.ticket.controller;

import com.flowops.ticket.Service.TicketCommentService;
import com.flowops.ticket.dto.request.AddcommentRequest;
import com.flowops.ticket.entity.TicketComment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class TicketCommentController {
    private final TicketCommentService commentService;

    @PostMapping
    public TicketComment addComment(@PathVariable UUID ticketId,
                                    @Valid @RequestBody AddcommentRequest request) {
        // Temporary actor.
        // Will be replaced by JWT SecurityContext.
        UUID authorId = UUID.randomUUID();

        return commentService.addComment(ticketId, request, authorId);
    }

    @GetMapping
    public List<TicketComment> getComments(@PathVariable UUID ticketId) {
        return commentService.getComments(ticketId);
    }
}
