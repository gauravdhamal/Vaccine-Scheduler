import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

// payment.js
document.addEventListener("DOMContentLoaded", () => {
  const paymentForm = document.getElementById("paymentForm");
  const paymentResultContainer = document.getElementById("paymentResult");

  paymentForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const appointmentId = document.getElementById("appointmentId").value;

    // Include other input fields for patient details
    const aadhaarNumber = document.getElementById("aadhaarNumber").value;
    const city = document.getElementById("city").value;
    const email = document.getElementById("email").value;
    const phone = document.getElementById("phone").value;
    const amount = document.getElementById("amount").value;
    const dateOfBirth = document.getElementById("dateOfBirth").value;
    const lastName = document.getElementById("lastName").value;
    const password = document.getElementById("password").value;
    const paymentMethod = document.getElementById("paymentMethod").value;
    const username = document.getElementById("username").value;

    const paymentData = {
      aadhaarNumber,
      address: {
        city,
        email,
        phone,
      },
      amount,
      dateOfBirth,
      lastName,
      password,
      paymentMethod,
      username,
    };

    console.log("paymentData : ", paymentData);

    try {
      const response = await fetch(
        `http://localhost:8888/payment/pay/${appointmentId}`,
        {
          method: "POST",
          headers: {
            Accept: "*/*",
            "Content-Type": "application/json",
          },
          body: JSON.stringify(paymentData),
        }
      );

      const result = await response.json();

      if (response.ok) {
        // Display success message
        displayPaymentDetails(result);
        window.alert(`Payment success. ID : ${result.paymentId}`);
      } else {
        if (result.message === "Validation failed.") {
          window.alert(`Description : ${result.description}`);
        } else if (result.message != undefined) {
          window.alert(`Error message : ${result.message}`);
        }
        // Display error message
      }
    } catch (error) {
      console.error("Error:", error);
      window.alert("An unexpected error occurred.");
    }
  });

  function displayPaymentDetails(paymentDetails) {
    const paymentDetailsHtml = `
        <h3>Payment Details</h3>
        <p><strong>Payment ID:</strong> ${paymentDetails.paymentId}</p>
        <p><strong>Created DateTime:</strong> ${paymentDetails.createdDateTime}</p>
        <p><strong>Amount:</strong> ${paymentDetails.amount}</p>
        <p><strong>Payment Method:</strong> ${paymentDetails.paymentMethod}</p>
        <p><strong>Transaction Status:</strong> ${paymentDetails.transactionStatus}</p>
        <p><strong>Patient ID:</strong> ${paymentDetails.patientId}</p>
        <p><strong>Patient FirstName:</strong> ${paymentDetails.patientFirstName}</p>
        <p><strong>Patient Username:</strong> ${paymentDetails.patientUsername}</p>
      `;

    paymentResultContainer.innerHTML = paymentDetailsHtml;
  }
});
