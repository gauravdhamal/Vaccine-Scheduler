import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

// Helper function to get the value of a cookie by name
function getJWTFromCookie(name) {
  const cookieArray = document.cookie.split(";");
  console.log("Cookie : ", document.cookie.split(";"));
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === name) {
      return cookieValue;
    }
  }
  return null;
}

function getUsernameFromCookie(loggedInUsername) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === loggedInUsername) {
      return cookieValue;
    }
  }
  return null;
}

function getExpirationDate(expirationDate) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === expirationDate) {
      return cookieValue;
    }
  }
  return null;
}

function getIsExpired(isExpired) {
  const cookieArray = document.cookie.split(";");
  for (const cookie of cookieArray) {
    const [cookieName, cookieValue] = cookie.trim().split("=");
    if (cookieName === isExpired) {
      return cookieValue;
    }
  }
  return null;
}

// Example usage in another JavaScript file:
const jwtToken = getJWTFromCookie("jwtToken");

if (jwtToken) {
  // Use the JWT token as needed
  console.log("JWT Token:", jwtToken);
  const loggedInUsername = getUsernameFromCookie("loggedInUsername");
  const expiryDate = getExpirationDate("expirationDate");
  const isExpired = getIsExpired("isExpired");
  let currDate = new Date().toUTCString();
  console.log("Currdate : ", currDate);
  console.log("expiryDate : ", expiryDate);
  console.log("isExpired : ", isExpired);
  console.log("Username : ", loggedInUsername);

  if (isExpired === true || isExpired === null) {
    window.alert(`Session expired.!!! Please login again.`);
    window.location.href = "/html/login.html";
  } else {
    const welcomeMessage = document.querySelector(".welcome-message h2");
    welcomeMessage.innerHTML = `Welcome '${loggedInUsername}'`;

    let form = document.getElementById("availabilityForm");

    form.addEventListener("submit", (event) => {
      event.preventDefault();
      let formData = new FormData(form);

      const patientId = formData.get("patientId");

      let pressedButton = document.activeElement;

      if (pressedButton.id === "getAppointmentsBtn") {
        console.log("Get Appointments button pressed");
        getAppointments();
      } else if (pressedButton.id === "getVaccinationDetailsBtn") {
        console.log("Get Vaccination Details button pressed");
        getVaccinationDetails();
      }
    });

    const headers = new Headers();
    headers.append("Authorization", `${jwtToken}`);

    function getAppointments() {
      const url = `http://localhost:8888/patient/appointments/${loggedInUsername}`;
      const tableHeader = document.querySelector(".container thead");
      const tableBody = document.querySelector(".container tbody");

      fetch(url, {
        method: "GET",
        headers: headers,
      })
        .then(async (response) => {
          const data = await response.json();

          if (response.ok) {
            tableHeader.innerHTML = "";
            const headers = [
              "Appointment ID",
              "Aadhaar Number",
              "Vaccine Name",
              "Dose",
              "Appointment Date",
              "Appointment Time",
              "Payment Amount",
              "Payment Status",
            ];
            const headRow = document.createElement("tr");
            headers.forEach((header) => {
              const th = document.createElement("th");
              th.innerHTML = header;
              headRow.appendChild(th);
            });
            tableHeader.appendChild(headRow);

            tableBody.innerHTML = "";
            data.forEach((appointment) => {
              const row = document.createElement("tr");
              row.innerHTML = `
          <td>${appointment.appointmentDetailId}</td>
          <td>${appointment.patientAadhaarNumber}</td>
          <td>${appointment.vaccineName}</td>
          <td>${appointment.doseNumber}</td>
          <td>${appointment.appointmentDate}</td>
          <td>${appointment.appointmentTime}</td>
          <td>${appointment.paymentDetailAmount}</td>
          <td>${appointment.paymentDetailTransactionStatus}</td>
        `;
              tableBody.appendChild(row);
            });
          } else {
            window.alert(`${data.message}`);
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          window.alert(`Error : ${error.message}`);
        });
    }

    function getVaccinationDetails() {
      const url = `http://localhost:8888/patient/vaccinations/${loggedInUsername}`;
      const tableBody = document.querySelector(".container tbody");
      const tableHeader = document.querySelector(".container thead");

      fetch(url, {
        method: "GET",
        headers: headers,
      })
        .then(async (response) => {
          const data = await response.json();

          if (response.ok) {
            tableHeader.innerHTML = "";
            const headers = [
              "Vaccination ID",
              "Aadhaar Number",
              "Vaccine Name",
              "Dose",
              "Vaccinated Date",
              "Vaccinated Time",
              "Vaccination Status",
              "Next VaccinationDate",
            ];
            const headRow = document.createElement("tr");
            headers.forEach((header) => {
              const th = document.createElement("th");
              th.innerHTML = header;
              headRow.appendChild(th);
            });
            tableHeader.appendChild(headRow);

            tableBody.innerHTML = "";
            data.forEach((vaccination) => {
              const row = document.createElement("tr");
              row.innerHTML = `
          <td>${vaccination.vaccinationDetailId}</td>
          <td>${vaccination.patientAadhaarNumber}</td>
          <td>${vaccination.vaccineName}</td>
          <td>${vaccination.doseNumber}</td>
          <td>${vaccination.vaccinatedDate}</td>
          <td>${vaccination.vaccinatedTime}</td>
          <td>${vaccination.vaccinationStatus}</td>
          <td>${vaccination.nextVaccinationDate}</td>
        `;
              tableBody.appendChild(row);
            });
          } else {
            tableHeader.innerHTML = "";
            tableBody.innerHTML = "";
            window.alert(data.message);
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          window.alert(`Error : ${error.message}`);
        });
    }

    const logoutBtn = document.getElementById("logout-button");
    logoutBtn.addEventListener("click", (event) => {
      event.preventDefault();
      console.log("Logout button pressed");
      document.cookie = "jwtToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
      document.cookie =
        "loggedInUsername=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
      document.cookie =
        "expirationDate=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
      document.cookie = "isExpired=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
      console.log("Cookie : ", document.cookie.split(";"));
      window.alert(`Thanks '${loggedInUsername}'. You have been logged out.`);
      window.location.href = "/html/index.html";
    });
  }
} else {
  console.log("JWT Token not found.");
  window.alert(`Session expired.!!! Please login again.`);
  window.location.href = "/html/login.html";
}
