package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.SlotRequest;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Slot;
import com.vaccinescheduler.models.Vaccine;
import com.vaccinescheduler.repositories.SlotRepo;
import com.vaccinescheduler.repositories.VaccineRepo;
import com.vaccinescheduler.services.SlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    private SlotRepo slotRepo;
    @Autowired
    private VaccineRepo vaccineRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public SlotResponse createSlot(SlotRequest slotRequest) throws GeneralException {
        Integer vaccineId = slotRequest.getVaccineId();
        Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
        if(vaccineById.isPresent()) {
            Vaccine vaccine = vaccineById.get();
            Slot slot = modelMapper.map(slotRequest, Slot.class);
            slot.setVaccine(vaccine);
            slot = slotRepo.save(slot);
            SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
            slotResponse.setSlotTiming(slot.getStartTime()+" - "+slot.getEndTime());
            return slotResponse;
        } else {
            throw new GeneralException("Vaccine not found with Id : { "+vaccineId+" }. Enter correct ID.");
        }
    }

    @Override
    public SlotResponse getSlot(Integer slotId) throws GeneralException {
        Optional<Slot> slotById = slotRepo.findById(slotId);
        if(slotById.isPresent()) {
            Slot slot = slotById.get();
            SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
            slotResponse.setSlotTiming(slot.getStartTime()+" - "+slot.getEndTime());
            return slotResponse;
        } else {
            throw new GeneralException("Slot not fount with Id : "+slotId);
        }
    }

    @Override
    public SlotResponse updateSlot(Integer slotId, SlotRequest slotRequest) throws GeneralException {
        Optional<Slot> slotById = slotRepo.findById(slotId);
        if(slotById.isPresent()) {
            Slot oldSlot = slotById.get();
            Slot updatedSlot = modelMapper.map(slotRequest, Slot.class);
            if(slotRequest.getVaccineId() != null) {
                Optional<Vaccine> vaccineById = vaccineRepo.findById(slotRequest.getVaccineId());
                if(vaccineById.isPresent()) {
                    Vaccine vaccine = vaccineById.get();
                    updatedSlot.setVaccine(vaccine);
                }
            }
            if(updatedSlot.getSlotDate() != null) {
                oldSlot.setSlotDate(updatedSlot.getSlotDate());
            }
            if(updatedSlot.getStartTime() != null) {
                oldSlot.setStartTime(updatedSlot.getStartTime());
            }
            if(updatedSlot.getEndTime() != null) {
                oldSlot.setEndTime(updatedSlot.getEndTime());
            }
            if(updatedSlot.getAvailableCount() != null) {
                oldSlot.setAvailableCount(updatedSlot.getAvailableCount());
            }
            if(updatedSlot.getVaccine() != null) {
                oldSlot.setVaccine(updatedSlot.getVaccine());
            }
            oldSlot = slotRepo.save(oldSlot);
            SlotResponse slotResponse = modelMapper.map(oldSlot, SlotResponse.class);
            slotResponse.setSlotTiming(oldSlot.getStartTime()+" - "+oldSlot.getEndTime());
            return slotResponse;
        } else {
            throw new GeneralException("Slot not fount with Id : "+slotId);
        }
    }

    @Override
    public Boolean deleteSlot(Integer slotId) throws GeneralException {
        Optional<Slot> slotById = slotRepo.findById(slotId);
        if(slotById.isPresent()) {
            Slot slot = slotById.get();
            slotRepo.delete(slot);
            return true;
        } else {
            throw new GeneralException("Slot not fount with Id : "+slotId);
        }
    }

    @Override
    public List<SlotResponse> getAllSlots() throws GeneralException {
        List<Slot> slots = slotRepo.findAll();
        if(!slots.isEmpty()) {
            List<SlotResponse> slotResponses = new ArrayList<>();
            for(Slot slot : slots) {
                SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
                slotResponse.setSlotTiming(slot.getStartTime()+" - "+slot.getEndTime());
                slotResponses.add(slotResponse);
            }
            return slotResponses;
        } else {
            throw new GeneralException("No any slot found in database.");
        }
    }

    @Override
    public List<SlotResponse> getAllSlotsByVaccineName(String vaccineName) throws GeneralException {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<Slot> slotsByVaccineVaccineName = slotRepo.findByVaccineVaccineNameAndSlotDateAfterOrSlotDateEqualsAndStartTimeAfter(vaccineName, currentDate, currentDate, currentTime);
        if(!slotsByVaccineVaccineName.isEmpty()) {
            List<SlotResponse> slotResponses = new ArrayList<>();
            for(Slot slot : slotsByVaccineVaccineName) {
                SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
                slotResponse.setSlotTiming(slot.getStartTime()+" - "+slot.getEndTime());
                slotResponses.add(slotResponse);
            }
            return slotResponses;
        } else {
            throw new GeneralException("No any slots available for vaccine : "+vaccineName);
        }
    }
}
