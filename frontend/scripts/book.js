import navbar from "../components/navbar.js";

document.getElementById("navbar").innerHTML = navbar();

document
  .getElementById("availabilityForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const vaccineName = document.getElementById("vaccineName").value;
    fetchSlots(vaccineName);
  });

async function fetchSlots(vaccineName) {
  try {
    const response = await fetch(
      `http://localhost:8888/slot/getSlotsByVaccineName/${vaccineName}`
    );
    const slots = await response.json();
    displaySlots(slots);
  } catch (error) {
    console.error("Error fetching slots:", error);
    window.alert(`Error : ${error.message}`);
  }
}

function displaySlots(slots) {
  const slotsBody = document.getElementById("slotsBody");
  slotsBody.innerHTML = "";
  if (slots.length === undefined || slots.length === 0) {
    slotsBody.innerHTML =
      "<tr><td colspan='8'>No slots available for the given vaccine.</td></tr>";
    return;
  }

  slots.forEach((slot) => {
    const row = document.createElement("tr");

    const staticColumns = [
      "slotId",
      "slotDate",
      "slotTiming",
      "doctorHospitalHospitalName",
      "vaccineName",
      "availableSlots",
    ];
    staticColumns.forEach((column) => {
      const cell = document.createElement("td");
      cell.textContent = slot[column];
      row.appendChild(cell);
    });

    const showDetailsButtonCell = row.insertCell();
    const showDetailsButton = document.createElement("button");
    showDetailsButton.textContent = "Show";
    showDetailsButton.addEventListener("click", () => showDetails(slot));
    showDetailsButtonCell.appendChild(showDetailsButton);

    const bookAppointmentButtonCell = row.insertCell();
    const bookAppointmentButton = document.createElement("button");
    bookAppointmentButton.textContent = "Book";
    bookAppointmentButton.addEventListener("click", () =>
      showBookAppointmentForm(slot)
    );
    bookAppointmentButtonCell.appendChild(bookAppointmentButton);

    slotsBody.appendChild(row);
  });
}

function showDetails(slot) {
  const overlay = document.createElement("div");
  overlay.classList.add("overlay");

  const detailsContainer = document.createElement("div");
  detailsContainer.classList.add("details-container");

  const keyValuePairs = [
    ["Slot Number", slot.slotId],
    ["Slot Date", slot.slotDate],
    ["Slot Timing", slot.slotTiming],
    ["Available Slots", slot.availableSlots],
    // ["Doctor Id", slot.doctorId],
    ["Doctor Name", `Dr. ${slot.doctorFirstName}`],
    // ["Doctor Username", slot.doctorUsername],
    // ["Vaccine Id", slot.vaccineId],
    ["Vaccine Name", slot.vaccineName],
    ["Vaccine Original Price", slot.vaccineOriginalPrice],
    ["Vaccine Discount", slot.vaccineDiscount],
    ["Required Age Range", slot.requiredAgeRange],
    // ["Hospital Id", slot.doctorHospitalHospitalId],
    ["Hospital Name", slot.doctorHospitalHospitalName],
  ];

  keyValuePairs.forEach(([key, value]) => {
    const detailItem = document.createElement("div");
    detailItem.innerHTML = `<strong>${key}:</strong> ${value}`;
    detailsContainer.appendChild(detailItem);
  });

  const closeBtn = document.createElement("button");
  closeBtn.textContent = "Close";
  closeBtn.addEventListener("click", () => {
    overlay.remove();
    detailsContainer.remove();
  });
  detailsContainer.appendChild(closeBtn);

  overlay.appendChild(detailsContainer);
  document.body.appendChild(overlay);
}

function showBookAppointmentForm(slot) {
  console.log("slot : ", slot);
  const hospitalId = slot.doctorHospitalHospitalId;

  const overlay = document.getElementById("hidden");
  const slotIdField = document.getElementById("slotIdField");
  const bookingForSelect = document.getElementById("bookingFor");
  const doseNumberSelect = document.getElementById("doseNumber");
  const genderSelect = document.getElementById("gender");
  const ageInput = document.getElementById("age");
  const emailInput = document.getElementById("email");
  const firstNameInput = document.getElementById("firstName");
  const phoneInput = document.getElementById("phone");
  const confirmBtn = document.getElementById("confirmBtn");
  const cancelBtn = document.getElementById("cancelBtn");

  let slotId = slot.slotId;
  slotIdField.innerHTML = `<strong>Slot Number : </strong> ${slotId}`;

  const formData = {
    bookingFor: "",
    doseNumber: "",
    gender: "",
    age: "",
    email: "",
    firstName: "",
    phone: "",
  };

  const updateFormData = (fieldId, value) => {
    formData[fieldId] = value;
  };

  // Add event listeners for select elements
  bookingForSelect.addEventListener("change", (event) => {
    updateFormData("bookingFor", event.target.value);
  });

  doseNumberSelect.addEventListener("change", (event) => {
    updateFormData("doseNumber", event.target.value);
  });

  genderSelect.addEventListener("change", (event) => {
    updateFormData("gender", event.target.value);
  });

  // Add event listeners for text inputs
  ageInput.addEventListener("input", (event) => {
    updateFormData("age", event.target.value);
  });

  emailInput.addEventListener("input", (event) => {
    updateFormData("email", event.target.value);
  });

  firstNameInput.addEventListener("input", (event) => {
    updateFormData("firstName", event.target.value);
  });

  phoneInput.addEventListener("input", (event) => {
    updateFormData("phone", event.target.value);
  });

  console.log("FormData : ", formData);

  // Add event listeners for buttons
  confirmBtn.addEventListener("click", () => {
    // Call the bookAppointment function with form data
    bookAppointment(slotId, hospitalId, formData);
    // Close the overlay
    overlay.style.display = "none";
  });

  cancelBtn.addEventListener("click", () => {
    // Close the overlay
    overlay.style.display = "none";
  });

  // Display the overlay
  overlay.style.display = "block";
}

async function bookAppointment(slotId, hospitalId, formData) {
  const bookUrl = `http://localhost:8888/appointment/book/${slotId}/${hospitalId}`;

  try {
    const response = await fetch(bookUrl, {
      method: "PUT",
      headers: {
        Accept: "*/*",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    const result = await response.json();

    if (response.ok) {
      // Display success message
      window.alert(result.message);
    } else {
      // Display error message
      if (
        result.message.startsWith(
          "You are not allowed to take this vaccine as your age"
        )
      ) {
        window.alert(result.message);
      } else if (result.message === "Validation failed.") {
        window.alert(result.description);
      } else {
        window.alert("An error occurred while processing your request.");
      }
    }
  } catch (error) {
    console.error("Error:", error);
  }
}
