package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExistingPaymentDetailRequest {
    @NotNull(message = "appointmentDetailId must not be null.")
    @Min(value = 1, message = "appointmentDetailId value must be greater than 0.")
    Integer appointmentDetailId;

    @NotNull(message = "Amount must not be null.")
    @DecimalMin(value = "0.0", message = "Amount must be greater than or equal to 0.0.")
    Double paidAmount;

    @NotNull(message = "paymentMethod must not null.")
    @NotEmpty(message = "paymentMethod must not empty.")
    @NotBlank(message = "paymentMethod must not blank.")
    @Pattern(regexp = "^(?i)(cash|card)$", message = "Invalid paymentMethod. Must be 'cash', or 'card'.")
    String paymentMethod;
}
