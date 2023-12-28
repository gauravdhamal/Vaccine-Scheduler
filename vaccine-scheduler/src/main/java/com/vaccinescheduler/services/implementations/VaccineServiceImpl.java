package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.VaccineRequest;
import com.vaccinescheduler.dtos.response.VaccineResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;
import com.vaccinescheduler.repositories.VaccineRepo;
import com.vaccinescheduler.services.VaccineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    private VaccineRepo vaccineRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public VaccineResponse createVaccine(VaccineRequest vaccineRequest) throws GeneralException {
        Vaccine vaccine = modelMapper.map(vaccineRequest, Vaccine.class);
        if(vaccine.getDiscount() != null) {
            StringBuilder tempDiscount = new StringBuilder(vaccine.getDiscount());
            if(tempDiscount.charAt(tempDiscount.length() - 1) == '%') {
                tempDiscount.deleteCharAt(tempDiscount.length() - 1);
                double discount = Double.parseDouble(tempDiscount.toString());
                Double originalPrice = vaccine.getOriginalPrice();
                Double priceAfterDiscount = originalPrice - (originalPrice * (discount / 100.00));
                vaccine.setDiscountedPrice(priceAfterDiscount);
            } else {
                throw new GeneralException("Invalid discount value passed.");
            }
            LocalDate manufacturedDate = vaccine.getManufacturedDate();
            LocalDate expiryDate = manufacturedDate.plusYears(1);
            vaccine.setExpirationDate(expiryDate);
            vaccine = vaccineRepo.save(vaccine);
            VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
            vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+vaccine.getMaxAge());
            return vaccineResponse;
        }
        return null;
    }

    @Override
    public VaccineResponse getVaccine(Integer vaccineId) throws GeneralException {
        Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
        if(vaccineById.isPresent()) {
            Vaccine vaccine = vaccineById.get();
            VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
            vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+vaccine.getMaxAge());
            return vaccineResponse;
        } else {
            throw new GeneralException("Vaccine not found with ID : "+vaccineId);
        }
    }

    @Override
    public VaccineResponse updateVaccine(Integer vaccineId, VaccineRequest vaccineRequest) throws GeneralException {
        Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
        if(vaccineById.isPresent()) {
            Vaccine oldVaccine = vaccineById.get();
            Vaccine updatedVaccine = modelMapper.map(vaccineRequest, Vaccine.class);
            if(updatedVaccine.getVaccineName() != null) {
                oldVaccine.setVaccineName(updatedVaccine.getVaccineName());
            }
            if(updatedVaccine.getVaccineManufacturer() != null) {
                oldVaccine.setVaccineManufacturer(updatedVaccine.getVaccineManufacturer());
            }
            if(updatedVaccine.getOriginalPrice() != null) {
                oldVaccine.setOriginalPrice(updatedVaccine.getOriginalPrice());
            }
            if(updatedVaccine.getDiscount() != null) {
                oldVaccine.setDiscount(updatedVaccine.getDiscount());
                StringBuilder tempDiscount = new StringBuilder(updatedVaccine.getDiscount());
                if(tempDiscount.charAt(tempDiscount.length() - 1) == '%') {
                    tempDiscount.deleteCharAt(tempDiscount.length() - 1);
                    double discount = Double.parseDouble(tempDiscount.toString());
                    Double originalPrice = oldVaccine.getOriginalPrice();
                    Double priceAfterDiscount = originalPrice - (originalPrice * (discount / 100.00));
                    oldVaccine.setDiscountedPrice(priceAfterDiscount);
                }
            }
            if(updatedVaccine.getMaxAge() != null) {
                oldVaccine.setMaxAge(updatedVaccine.getMaxAge());
            }
            if(updatedVaccine.getMinAge() != null) {
                oldVaccine.setMinAge(updatedVaccine.getMinAge());
            }
            if(updatedVaccine.getBoosterDose()) {
                oldVaccine.setBoosterDose(updatedVaccine.getBoosterDose());
            }
            if(updatedVaccine.getDosesRequired() != null) {
                oldVaccine.setDosesRequired(updatedVaccine.getDosesRequired());
            }
            if(updatedVaccine.getDaysBetweenDoses() != null) {
                oldVaccine.setDaysBetweenDoses(updatedVaccine.getDaysBetweenDoses());
            }
            oldVaccine = vaccineRepo.save(oldVaccine);
            VaccineResponse vaccineResponse = modelMapper.map(oldVaccine, VaccineResponse.class);
            vaccineResponse.setAgeRange(oldVaccine.getMinAge()+" - "+oldVaccine.getMaxAge());
            return vaccineResponse;
        } else {
            throw new GeneralException("Vaccine not found with ID : "+vaccineId);
        }
    }

    @Override
    public Boolean deleteVaccine(Integer vaccineId) throws GeneralException {
        Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
        if(vaccineById.isPresent()) {
            Vaccine vaccine = vaccineById.get();
            vaccineRepo.delete(vaccine);
            return true;
        } else {
            throw new GeneralException("Vaccine not found with ID : "+vaccineId);
        }
    }

    @Override
    public List<VaccineResponse> getAllVaccines() throws GeneralException {
        List<Vaccine> vaccines = vaccineRepo.findAll();
        if(!vaccines.isEmpty()) {
            List<VaccineResponse> vaccineResponses = new ArrayList<>();
            for(Vaccine vaccine : vaccines) {
                VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
                vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+vaccine.getMaxAge());
                vaccineResponses.add(vaccineResponse);
            }
            return vaccineResponses;
        } else {
            throw new GeneralException("No any vaccine found in database.");
        }
    }

    @Override
    public List<VaccineResponse> getVaccinesByType(String type) throws GeneralException {
        Optional<List<Vaccine>> typeVaccines;
        if(type.toLowerCase().equals("adult")) {
            typeVaccines = vaccineRepo.getAdultVaccines();
        } else if(type.toLowerCase().equals("child")) {
            typeVaccines = vaccineRepo.getChildVaccines();
        } else {
            throw new GeneralException("Invalid type : '"+type+"'. Type must be either 'adult' or 'child'");
        }
        if(typeVaccines.isPresent() && !typeVaccines.get().isEmpty()) {
            List<Vaccine> vaccines = typeVaccines.get();
            List<VaccineResponse> vaccineResponses = new ArrayList<>();
            for(Vaccine vaccine : vaccines) {
                VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
                vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+ vaccine.getMaxAge());
                vaccineResponses.add(vaccineResponse);
            }
            return vaccineResponses;
        } else {
            throw new GeneralException("No any vaccines found for type : "+type);
        }
    }

    @Override
    public List<VaccineResponse> findVaccineByName(String vaccineName) throws GeneralException {
        Optional<List<Vaccine>> vaccinesByVaccineName = vaccineRepo.findByVaccineName(vaccineName);
        if(vaccinesByVaccineName.isPresent() && !vaccinesByVaccineName.get().isEmpty()) {
            List<Vaccine> vaccines = vaccinesByVaccineName.get();
            List<VaccineResponse> vaccineResponses = new ArrayList<>();
            for(Vaccine vaccine : vaccines) {
                VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
                vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+vaccine.getMaxAge());
                vaccineResponses.add(vaccineResponse);
            }
            return vaccineResponses;
        } else {
            throw new GeneralException("No any vaccine found with name : "+vaccineName);
        }
    }
}
