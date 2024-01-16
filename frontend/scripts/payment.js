import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

function getPersonFromCookie(personCookieName) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === personCookieName) {
      return cookieValue;
    }
  }
  return null;
}

function getAppointmentIdFromCookie(appointmentIdCookieName) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === appointmentIdCookieName) {
      return cookieValue;
    }
  }
  return null;
}

function isExistingUserFromCookie(existingUserCookieName) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === existingUserCookieName) {
      return cookieValue;
    }
  }
  return null;
}

document.addEventListener("DOMContentLoaded", function () {
  let existingUser = isExistingUserFromCookie("existingUser");
  existingUser = JSON.parse(decodeURIComponent(existingUser));

  const paymentForm = document.getElementById("paymentForm");

  const appointmentIdField = document.getElementById("appointmentId");
  const usernameField = document.getElementById("username");
  const firstNameField = document.getElementById("firstName");
  const genderField = document.getElementById("gender");
  const emailField = document.getElementById("email");
  const phoneField = document.getElementById("phone");
  const amountField = document.getElementById("amount");
  const passwordField = document.getElementById("password");
  const aadhaarNumberField = document.getElementById("aadhaarNumber");
  const cityField = document.getElementById("city");
  const dateOfBirthField = document.getElementById("dateOfBirth");
  const lastNameField = document.getElementById("lastName");
  const ageField = document.getElementById("age");

  if (existingUser === true) {
    console.log("user already present");

    const username = getPersonFromCookie("username");
    console.log("username : " + username);

    const appointmentId = getAppointmentIdFromCookie("appointmentId");
    console.log("appointmentId : " + appointmentId);

    appointmentIdField.value = appointmentId;
    usernameField.value = username;

    fetchPersonDetails(username).then((person) => {
      firstNameField.value = person.firstName;
      genderField.value = person.gender;
      emailField.value = person.address.email;
      phoneField.value = person.address.phone;
      passwordField.value = "hiddenPassword";
      aadhaarNumberField.value = person.aadhaarNumber;
      cityField.value = person.address.city;
      dateOfBirthField.value = person.dateOfBirth;
      lastNameField.value = person.lastName;
      ageField.value = person.age;

      aadhaarNumberField.readOnly = true;
      cityField.readOnly = true;
      dateOfBirthField.readOnly = true;
      lastNameField.readOnly = true;
      usernameField.readOnly = true;
      passwordField.readOnly = true;
    });

    fetchAppointmentData(appointmentId).then((appointment) => {
      amountField.value = appointment.vaccineDiscountedPrice;
    });

    paymentForm.addEventListener("submit", (event) => {
      event.preventDefault();
      console.log("paymentForm called from if block : ");
      payForExistingUser().then((boolean) => {
        if (boolean === true) {
          console.log("Resetting payment form.");
          paymentForm.reset();
        }
      });
    });
  } else {
    console.log("Create new user");

    const appointmentId = getAppointmentIdFromCookie("appointmentId");
    console.log("appointmentId : " + appointmentId);

    appointmentIdField.value = appointmentId;

    fetchAppointmentData(appointmentId).then((appointment) => {
      firstNameField.value = appointment.firstName;
      genderField.value = appointment.gender;
      emailField.value = appointment.email;
      phoneField.value = appointment.phone;
      amountField.value = appointment.vaccineDiscountedPrice;
      ageField.value = appointment.age;
      dateOfBirthField.value = appointment.dateOfBirth;
    });

    paymentForm.addEventListener("submit", (event) => {
      event.preventDefault();
      console.log("paymentForm called from else block : ");
      payForNewUser().then((boolean) => {
        if (boolean === true) {
          console.log("Resetting payment form.");
          paymentForm.reset();
        }
      });
    });
  }
});

async function payForNewUser() {
  const appointmentId = document.getElementById("appointmentId").value;
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
      displayPaymentDetails(result);
      document.cookie = `appointmentId=null;`;
      document.cookie = `username=null;`;
      document.cookie = `existingUser=null;`;
      window.alert(`Payment success. ID : ${result.paymentId}`);
      return true;
    } else {
      if (result.message === "Validation failed.") {
        window.alert(`Description : ${result.description}`);
      } else if (result.message != undefined) {
        window.alert(`Error message : ${result.message}`);
      }
    }
  } catch (error) {
    console.error("Error:", error);
    window.alert("An unexpected error occurred.", error.message);
  }
}

async function payForExistingUser() {
  const paymentMethod = document.getElementById("paymentMethod").value;
  const appointmentDetailId = document.getElementById("appointmentId").value;
  const paidAmount = document.getElementById("amount").value;
  const username = document.getElementById("username").value;

  let paymentData = {
    appointmentDetailId,
    paidAmount,
    paymentMethod,
  };

  console.log("paymentData : ", paymentData);

  const uri = `http://localhost:8888/payment/pay/existing/${username}`;

  try {
    const response = await fetch(uri, {
      method: "POST",
      headers: {
        Accept: "*/*",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(paymentData),
    });

    const result = await response.json();

    if (response.ok) {
      displayPaymentDetails(result);
      document.cookie = `appointmentId=null;`;
      document.cookie = `username=null;`;
      document.cookie = `existingUser=null;`;
      window.alert(`Payment success. ID : ${result.paymentId}`);
      return true;
    } else {
      if (result.message === "Validation failed.") {
        window.alert(`Description : ${result.description}`);
      } else if (result.message != undefined) {
        window.alert(`Error message : ${result.message}`);
      }
    }
  } catch (error) {
    console.error("Error:", error);
    window.alert("An unexpected error occurred.", error.message);
  }
}

function displayPaymentDetails(paymentDetails) {
  const paymentResultContainer = document.getElementById("paymentResult");
  paymentResultContainer.style.display = "block";

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

async function fetchPersonDetails(username) {
  var requestOptions = {
    method: "GET",
    redirect: "follow",
  };

  try {
    const response = await fetch(
      `http://localhost:8888/person/byUsername/${username}`,
      requestOptions
    );
    const data = await response.json();

    if (response.ok) {
      return data;
    } else {
      window.alert(`${data.message}\n\nEnter valid username.`);
    }
  } catch (error) {
    console.log("error", error);
    window.alert("An unexpected error occurred.", error.message);
    throw error;
  }
}

async function fetchAppointmentData(appointmentId) {
  var requestOptions = {
    method: "GET",
    redirect: "follow",
  };

  try {
    const response = await fetch(
      `http://localhost:8888/appointment/get/${appointmentId}`,
      requestOptions
    );
    const appointmentData = await response.json();

    if (response.ok) {
      console.log("appointmentData : ", appointmentData);
      return appointmentData;
    } else {
      window.alert(appointmentData.message);
    }
  } catch (error) {
    console.log("error", error);
    window.alert("An unexpected error occurred.", error.message);
    throw error;
  }
}
