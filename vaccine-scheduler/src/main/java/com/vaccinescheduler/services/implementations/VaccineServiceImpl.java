package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.VaccineRequest;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;
import com.vaccinescheduler.repositories.VaccineRepo;
import com.vaccinescheduler.services.VaccineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    private VaccineRepo vaccineRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Vaccine createVaccine(VaccineRequest vaccineRequest) throws GeneralException {
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
            return vaccineRepo.save(vaccine);
        }
        return null;
    }

    @Override
    public Vaccine getVaccine(Integer vaccineId) throws GeneralException {
        Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
        if(vaccineById.isPresent()) {
            Vaccine vaccine = vaccineById.get();
            return vaccine;
        } else {
            throw new GeneralException("Vaccine not found with ID : "+vaccineId);
        }
    }

    @Override
    public Vaccine updateVaccine(Integer vaccineId, VaccineRequest vaccineRequest) throws GeneralException {
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
            oldVaccine = vaccineRepo.save(oldVaccine);
            Vaccine vaccine = modelMapper.map(oldVaccine, Vaccine.class);
            return vaccine;
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
}
