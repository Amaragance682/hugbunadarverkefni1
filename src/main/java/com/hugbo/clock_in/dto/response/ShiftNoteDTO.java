package com.hugbo.clock_in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftNoteDTO {
    public Long id;
    public Long shiftId;
    public String note;
    public UserDTO createdBy;
}
