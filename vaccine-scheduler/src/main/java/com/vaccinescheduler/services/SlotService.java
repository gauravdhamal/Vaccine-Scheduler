package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.SlotRequest;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Slot;

import java.util.List;

public interface SlotService {
    SlotResponse createSlot(SlotRequest slotRequest) throws GeneralException;
    SlotResponse getSlot(Integer slotId) throws GeneralException;
    SlotResponse updateSlot(Integer slotId, SlotRequest slotRequest) throws GeneralException;
    Boolean deleteSlot(Integer slotId) throws GeneralException;
    List<SlotResponse> getAllSlots() throws GeneralException;
    List<SlotResponse> getAllSlotsByVaccineName(String vaccineName) throws GeneralException;
}
