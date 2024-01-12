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

    const appointmentId = appointmentDetails.appointmentDetailId;
    appointmentIdContent.innerHTML = appointmentId;
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
      appointmentPaymentAmtContent.innerHTML = `Rs. ${paymentAmt}`;
    }

    if (paymentAmt === null) {
      makePaymentBtn.style.display = "block";
      makePaymentBtn.addEventListener("click", () =>
        displayOptions(appointmentId)
      );
    } else {
      makePaymentBtn.style.display = "none";
    }
  }

  function displayOptions(appointmentId) {
    const overlay = document.getElementById("displayOverlay");
    const detailsContainer = document.getElementById("displayDetailsContainer");
    overlay.style.display = "block";

    const existingUserButton = document.getElementById("existingUserButton");
    const newUserButton = document.getElementById("newUserButton");

    const existingUserOption = document.getElementById("existingUserOption");
    const proceedPaymentButton = document.getElementById(
      "proceedPaymentButton"
    );
    const optionFormAptIdLabel = document.getElementById(
      "optionFormAptIdLabel"
    );
    const optionFormAptIdInput = document.getElementById(
      "optionFormAptIdInput"
    );
    const optionFormUsernameLabel = document.getElementById(
      "optionFormUsernameLabel"
    );
    const optionFormUsernameInput = document.getElementById(
      "optionFormUsernameInput"
    );
    const optionFormProceedInput = document.getElementById(
      "optionFormProceedInput"
    );

    optionFormAptIdInput.value = appointmentId;

    existingUserButton.addEventListener("click", () => {
      existingUserButton.style.backgroundColor = "green";
      newUserButton.style.backgroundColor = "red";
      proceedPaymentButton.style.display = "none";
      existingUserOption.style.display = "block";
      optionFormAptIdLabel.style.display = "block";
      optionFormAptIdInput.style.display = "block";
      optionFormUsernameLabel.style.display = "block";
      optionFormUsernameInput.style.display = "block";
      optionFormProceedInput.style.display = "block";
    });

    newUserButton.addEventListener("click", () => {
      newUserButton.style.backgroundColor = "green";
      existingUserButton.style.backgroundColor = "red";
      proceedPaymentButton.style.display = "block";
      existingUserOption.style.display = "block";
      optionFormAptIdLabel.style.display = "block";
      optionFormAptIdInput.style.display = "block";
      optionFormUsernameLabel.style.display = "none";
      optionFormUsernameInput.style.display = "none";
      optionFormProceedInput.style.display = "none";
    });

    proceedPaymentButton.addEventListener("click", () => {
      setCookie(appointmentId, null, false);
      window.alert("Redirecting to payment page...");
      window.location.href = "/html/payment.html";
    });

    const existingUserOptionForm = document.getElementById(
      "existingUserOptionForm"
    );
    existingUserOptionForm.addEventListener("submit", (event) => {
      event.preventDefault();
      let formData = new FormData(event.target);
      let username = formData.get("existinngUserUsername");
      let appointmentIds = formData.get("appointmentId");
      console.log("appointmentIds : ", appointmentIds);
      console.log("Username : ", username);

      var requestOptions = {
        method: "GET",
        redirect: "follow",
      };

      fetch(
        `http://localhost:8888/person/byUsername/${username}`,
        requestOptions
      )
        .then(async (response) => {
          const data = await response.json();
          if (response.ok) {
            let aadhaarNumber = data.aadhaarNumber;
            const confirmed = window.confirm(
              `'${aadhaarNumber}' is this your aadhaar number?`
            );
            if (confirmed) {
              setCookie(appointmentId, username, true);
              window.alert("Redirecting to payment page...");
              window.location.href = "/html/payment.html";
            } else {
              window.alert("Please enter the correct username to proceed.");
            }
            return data;
          } else {
            window.alert(`${data.message}\n\nEnter valid username.`);
          }
        })
        .then((result) => console.log(result))
        .catch((error) => {
          console.log("error", error);
          window.alert(`Error : ${error.message}`);
        });
    });

    const closeBtn = document.getElementById("displayOptionsButton");
    closeBtn.addEventListener("click", () => {
      overlay.style.display = "none";
      proceedPaymentButton.style.display = "none";
      existingUserOption.style.display = "none";
      newUserButton.style.backgroundColor = "rgb(219, 184, 120)";
      existingUserButton.style.backgroundColor = "rgb(219, 184, 120)";
      optionFormUsernameInput.value = "";
    });
    detailsContainer.appendChild(closeBtn);
  }

  function setCookie(appointmentId, username, isExistingUser) {
    console.log("Setting cookie");
    if (isExistingUser) {
      document.cookie = `appointmentId=${appointmentId};`;
      document.cookie = `username=${username};`;
      document.cookie = `existingUser=${encodeURIComponent(
        JSON.stringify(isExistingUser)
      )};`;
    } else {
      document.cookie = `appointmentId=${appointmentId};`;
      document.cookie = `username=${username};`;
      document.cookie = `existingUser=${encodeURIComponent(
        JSON.stringify(isExistingUser)
      )};`;
    }
  }
});
