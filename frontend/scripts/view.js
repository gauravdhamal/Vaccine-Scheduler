import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

document.addEventListener("DOMContentLoaded", () => {
  const viewAppointmentForm = document.getElementById("viewAppointmentForm");
  const appointmentDetailsContainer =
    document.getElementById("appointmentDetails");

  viewAppointmentForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const appointmentIdInput = document.getElementById("appointmentId");
    const appointmentId = appointmentIdInput.value;

    // Fetch appointment details
    const appointmentDetails = await getAppointmentDetails(appointmentId);

    // Display appointment details in the container
    if (appointmentDetails !== undefined) {
      displayAppointmentDetails(appointmentDetails);
    } else {
      appointmentDetailsContainer.innerHTML = "";
    }
  });

  async function getAppointmentDetails(appointmentId) {
    const apiUrl = `http://localhost:8888/appointment/get/${appointmentId}`;

    try {
      const response = await fetch(apiUrl, {
        method: "GET",
        headers: {
          Accept: "*/*",
        },
      });

      if (response.ok) {
        const result = await response.json();
        return result;
      } else {
        window.alert(`Invalid appointmentId : ${appointmentId}`);
      }
    } catch (error) {
      console.error("Error:", error);
      window.alert(`Error : ${error.message}`);
      // Handle error, maybe display an error message to the user
    }
  }

  function displayAppointmentDetails(appointmentDetails) {
    const appointmentDetailsHtml = `
        <h3>Appointment Details</h3>
        <p><strong>Appointment ID:</strong> ${appointmentDetails.appointmentDetailId}</p>
        <p><strong>Appointment Date:</strong> ${appointmentDetails.appointmentDate}</p>
        <p><strong>Appointment Time:</strong> ${appointmentDetails.appointmentTime}</p>
        <p><strong>Message:</strong> ${appointmentDetails.message}</p>
        <p><strong>Payment Detail ID:</strong> ${appointmentDetails.paymentDetailId}</p>
        <p><strong>Payment Detail Amount:</strong> ${appointmentDetails.paymentDetailAmount}</p>
      `;

    appointmentDetailsContainer.innerHTML = appointmentDetailsHtml;
  }
});
