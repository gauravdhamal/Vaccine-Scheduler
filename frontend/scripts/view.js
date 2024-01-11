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

    getAppointmentDetails(appointmentId);
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
        displayAppointmentDetails(result);
      } else {
        window.alert(`Invalid appointmentId : ${appointmentId}`);
        appointmentDetailsContainer.style.display = "none";
      }
    } catch (error) {
      console.error("Error:", error);
      window.alert(`Error : ${error.message}`);
    }
  }

  function displayAppointmentDetails(appointmentDetails) {
    appointmentDetailsContainer.style.innerHTML = "";
    appointmentDetailsContainer.style.display = "block";

    const appointmentIdContent = document.getElementById(
      "appointmentIdContent"
    );
    const appointmentDateContent = document.getElementById(
      "appointmentDateContent"
    );
    const appointmentTimeContent = document.getElementById(
      "appointmentTimeContent"
    );
    const appointmentMessageContent = document.getElementById(
      "appointmentMessageContent"
    );
    const appointmentPaymentIdContent = document.getElementById(
      "appointmentPaymentIdContent"
    );
    const appointmentPaymentAmtContent = document.getElementById(
      "appointmentPaymentAmtContent"
    );
    const makePaymentBtn = document.getElementById("makePayment");

    appointmentIdContent.innerHTML = appointmentDetails.appointmentDetailId;
    appointmentDateContent.innerHTML = appointmentDetails.appointmentDate;
    appointmentTimeContent.innerHTML = appointmentDetails.appointmentTime;
    appointmentMessageContent.innerHTML = appointmentDetails.message;

    const paymentId = appointmentDetails.paymentDetailId;
    const paymentAmt = appointmentDetails.paymentDetailAmount;
    if (paymentId === null || paymentAmt === null) {
      appointmentPaymentIdContent.innerHTML = "n/a";
      appointmentPaymentAmtContent.innerHTML = "n/a";
    } else {
      appointmentPaymentIdContent.innerHTML = paymentId;
      appointmentPaymentAmtContent.innerHTML = paymentAmt;
    }

    if (paymentAmt === null) {
      makePaymentBtn.style.display = "block";
      makePaymentBtn.addEventListener("click", () => optionDivs());
    } else {
      makePaymentBtn.style.display = "none";
    }
  }

  function optionDivs() {
    const overlay = document.getElementById("displayOverlay");
    const detailsContainer = document.getElementById("displayDetailsContainer");
    overlay.style.display = "block";

    const existingUserButton = document.getElementById("existingUserButton");
    const newUserButton = document.getElementById("newUserButton");
    const proceedPaymentButton = document.getElementById(
      "proceedPaymentButton"
    );
    const existingUserOption = document.getElementById("existingUserOption");
    const existinngUserUsernameInput = document.getElementById(
      "existinngUserUsername"
    );

    existingUserButton.addEventListener("click", () => {
      newUserButton.style.backgroundColor = "red";
      existingUserButton.style.backgroundColor = "green";
      proceedPaymentButton.style.display = "block";
      existingUserOption.style.display = "block";
    });

    newUserButton.addEventListener("click", () => {
      existingUserOption.style.display = "none";
      newUserButton.style.backgroundColor = "green";
      existingUserButton.style.backgroundColor = "red";
      proceedPaymentButton.style.display = "block";
    });

    const closeBtn = document.getElementById("displayOptionsButton");
    closeBtn.addEventListener("click", () => {
      overlay.style.display = "none";
      proceedPaymentButton.style.display = "none";
      existingUserOption.style.display = "none";
      newUserButton.style.backgroundColor = "rgb(219, 184, 120)";
      existingUserButton.style.backgroundColor = "rgb(219, 184, 120)";
      existinngUserUsernameInput.value = "";
    });
    detailsContainer.appendChild(closeBtn);
  }
});
