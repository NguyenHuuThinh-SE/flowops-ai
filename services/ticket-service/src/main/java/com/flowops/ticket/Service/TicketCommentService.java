package com.flowops.ticket.Service;

import com.flowops.ticket.dto.request.AddcommentRequest;
import com.flowops.ticket.entity.TicketComment;
import com.flowops.ticket.entity.TicketHistory;
import com.flowops.ticket.enums.TicketAction;
import com.flowops.ticket.exception.ResourceNotFoundException;
import com.flowops.ticket.repository.TicketCommentRepository;
import com.flowops.ticket.repository.TicketHistoryRepository;
import com.flowops.ticket.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketCommentService {
    private final TicketRepository ticketRepository;
    private final TicketCommentRepository commentRepository;
    private final TicketHistoryRepository historyRepository;

    @Transactional
    public TicketComment addComment(
            UUID ticketId,
            AddcommentRequest request,
            UUID authorId
    ) {

        ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found: " + ticketId
                        )
                );

        TicketComment comment = new TicketComment();

        comment.setTicketId(ticketId);
        comment.setAuthorId(authorId);
        comment.setContent(request.getContent());

        TicketComment saved =
                commentRepository.save(comment);

        TicketHistory history = new TicketHistory();

        history.setTicketId(ticketId);
        history.setActorId(authorId);
        history.setAction(TicketAction.COMMENTED);
        history.setDetails("Added a comment");

        historyRepository.save(history);

        return saved;
    }

    public List<TicketComment> getComments(
            UUID ticketId
    ) {
        ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found: " + ticketId
                        )
                );
        return commentRepository
                .findByTicketIdOrderByCreatedAtAsc(ticketId);
    }
}
