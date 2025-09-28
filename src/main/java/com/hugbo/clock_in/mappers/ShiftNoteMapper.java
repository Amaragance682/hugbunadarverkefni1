package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.ShiftNote;
import com.hugbo.clock_in.dto.response.ShiftNoteDTO;

@Component
public class ShiftNoteMapper {
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private UserMapper userMapper;
    public ShiftNoteDTO toDTO(ShiftNote shiftNote) {
        if (shiftNote == null) return null;
        return new ShiftNoteDTO(
            shiftNote.id,
            shiftNote.shift.id,
            shiftNote.note,
            userMapper.toDTO(shiftNote.createdBy)
        );
    }
}
