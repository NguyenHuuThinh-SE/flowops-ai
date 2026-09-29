package com.flowops.ticket.dto.request;

import com.flowops.ticket.enums.TicketCategory;
import com.flowops.ticket.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTicketRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TicketPriority priority;

    @NotNull
    private TicketCategory category;

    @Size(max = 100)
    private String department;

}
