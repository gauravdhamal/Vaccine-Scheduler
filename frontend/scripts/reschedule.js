import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

// reschedule.js

document
  .getElementById("rescheduleForm")
  .addEventListener("submit", function (event) {
    event.preventDefault(); // Prevents the form from submitting normally

    // Retrieve the values from the input fields
    const appointmentId = document.getElementById("appointmentId").value;
    const newSlotId = document.getElementById("newSlotId").value;

    // Call the function to reschedule appointment
    rescheduleAppointment(appointmentId, newSlotId);
  });

// Function to reschedule appointment
async function rescheduleAppointment(appointmentId, newSlotId) {
  const apiUrl = `http://localhost:8888/appointment/reschedule/${newSlotId}/${appointmentId}`;

  try {
    const response = await fetch(apiUrl, {
      method: "PUT",
      headers: {
        Accept: "*/*",
      },
    });

    if (response.ok) {
      const result = await response.json();
      // Display the result in the container
      displayRescheduleResult(result);
    } else {
      const result = await response.json();

      // Handle error response
      if (result.message !== undefined) {
        displayRescheduleError(result.message);
      } else {
        console.error("Error:", result.message);
        displayRescheduleError("An unexpected error occurred.");
      }
    }
  } catch (error) {
    displayRescheduleError(`An unexpected error occurred. ${error.message}`);
  }
}

// Function to display reschedule result
function displayRescheduleResult(result) {
  const resultContainer = document.getElementById("rescheduleResult");

  if (result.appointmentDetailId) {
    const rescheduleHtml = `
        <h3>Rescheduled Appointment Details</h3>
        <p><strong>Appointment ID:</strong> ${result.appointmentDetailId}</p>
        <p><strong>New Appointment Date:</strong> ${result.appointmentDate}</p>
        <p><strong>New Appointment Time:</strong> ${result.appointmentTime}</p>
        <p><strong>Message:</strong> ${result.message}</p>
        <p><strong>Payment Detail ID:</strong> ${result.paymentDetailId}</p>
        <p><strong>Payment Detail Amount:</strong> ${result.paymentDetailAmount}</p>
      `;

    resultContainer.innerHTML = rescheduleHtml;
  } else {
    displayRescheduleError("An unexpected error occurred.");
  }
}

// Function to display reschedule error
function displayRescheduleError(errorMessage) {
  const resultContainer = document.getElementById("rescheduleResult");
  resultContainer.innerHTML = `<p style="color: red;">${errorMessage}</p>`;
}
