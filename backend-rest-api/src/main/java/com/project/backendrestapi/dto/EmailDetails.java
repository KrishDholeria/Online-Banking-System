package com.project.backendrestapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
