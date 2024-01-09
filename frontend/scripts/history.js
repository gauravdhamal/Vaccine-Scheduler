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

  if (isExpired === true) {
    window.alert(`Session expired.!!! Please login again.`);
    window.location.href = "/html/login.html";
  } else {
    let form = document.getElementById("availabilityForm");

    form.addEventListener("submit", (event) => {
      event.preventDefault();
      let formData = new FormData(form);

      const patientId = formData.get("patientId");

      let pressedButton = document.activeElement;

      if (pressedButton.id === "getAppointmentsBtn") {
        console.log("Get Appointments button pressed");
        getAppointments(patientId);
      } else if (pressedButton.id === "getVaccinationDetailsBtn") {
        console.log("Get Vaccination Details button pressed");
        getVaccinationDetails(patientId);
      }
    });

    const headers = new Headers();
    headers.append("Authorization", `${jwtToken}`);

    function getAppointments(patientId) {
      const url = `http://localhost:8888/patient/appointments/${patientId}`;
      const tableBody = document.querySelector(".container tbody");

      fetch(url, {
        method: "GET",
        headers: headers,
      })
        .then((response) => response.json())
        .then((data) => {
          tableBody.innerHTML = "";
          data.forEach((appointment) => {
            const row = document.createElement("tr");
            row.innerHTML = `
          <td>${appointment.appointmentDetailId}</td>
          <td>${appointment.appointmentDate}</td>
          <td>${appointment.appointmentTime}</td>
          <td>${appointment.doseNumber}</td>
          <td>${appointment.vaccineName}</td>
          <td>${appointment.paymentDetailTransactionStatus}</td>
          <td><button onclick="viewDetails(${appointment.appointmentDetailId}, 'appointments')">View More</button></td>
        `;
            tableBody.appendChild(row);
          });
        })
        .catch((error) => console.error("Error:", error));
    }

    function getVaccinationDetails(patientId) {
      const url = `http://localhost:8888/patient/vaccinations/${patientId}`;
      const tableBody = document.querySelector(".container tbody");

      fetch(url, {
        method: "GET",
        headers: headers,
      })
        .then((response) => response.json())
        .then((data) => {
          tableBody.innerHTML = "";
          data.forEach((vaccination) => {
            const row = document.createElement("tr");
            row.innerHTML = `
          <td>${vaccination.vaccinationDetailId}</td>
          <td>${vaccination.vaccinatedDate}</td>
          <td>${vaccination.vaccinatedTime}</td>
          <td>${vaccination.doseNumber}</td>
          <td>${vaccination.vaccineName}</td>
          <td>${vaccination.vaccinationStatus}</td>
          <td><button onclick="viewDetails(${vaccination.vaccinationDetailId}, 'vaccinations')">View More</button></td>
        `;
            tableBody.appendChild(row);
          });
        })
        .catch((error) => console.error("Error:", error));
    }
  }
} else {
  console.log("JWT Token not found.");
  window.alert(`Session expired.!!! Please login again.`);
  window.location.href = "/html/login.html";
}
