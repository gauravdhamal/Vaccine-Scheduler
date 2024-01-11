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
      "<tr><td colspan='8'>No slots available for the selected vaccine.</td></tr>";
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
  const overlay = document.getElementById("displayOverlay");
  const detailsContainer = document.getElementById("displayDetailsContainer");
  detailsContainer.innerHTML = "";
  overlay.style.display = "block";

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
    overlay.style.display = "none";
  });
  detailsContainer.appendChild(closeBtn);

  overlay.appendChild(detailsContainer);
  document.body.appendChild(overlay);
}

function showBookAppointmentForm(slot) {
  const hospitalId = slot.doctorHospitalHospitalId;

  const overlay = document.getElementById("hidden");
  const slotIdField = document.getElementById("slotIdField");
  const bookAppointmentForm = document.getElementById("bookAppointmentForm");
  const cancelBtn = document.getElementById("cancelBtn");

  let slotId = slot.slotId;
  slotIdField.value = `${slotId}`;

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
  const selectElements = ["bookingFor", "doseNumber", "gender"];
  selectElements.forEach((elementId) => {
    const selectElement = document.getElementById(elementId);
    selectElement.addEventListener("change", (event) => {
      updateFormData(elementId, event.target.value);
    });
  });

  // Add event listeners for text inputs
  const textInputs = ["age", "email", "firstName", "phone"];
  textInputs.forEach((inputId) => {
    const inputElement = document.getElementById(inputId);
    inputElement.addEventListener("input", (event) => {
      updateFormData(inputId, event.target.value);
    });
  });

  // Add event listener for the form
  bookAppointmentForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const resultPromise = bookAppointment(slotId, hospitalId, formData);
    resultPromise.then((result) => {
      if (result === "Appointment booked successfully.") {
        bookAppointmentForm.reset();
        overlay.style.display = "none";
        fetchSlots(slot.vaccineName);
      }
    });
  });

  cancelBtn.addEventListener("click", () => {
    bookAppointmentForm.reset();
    overlay.style.display = "none";
  });

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
      window.alert(result.message);
      return "Appointment booked successfully.";
    } else {
      if (
        result.message.startsWith(
          "You are not allowed to take this vaccine as your age"
        )
      ) {
        window.alert(result.message);
      } else if (result.message === "Validation failed.") {
        window.alert(result.description);
      } else if (
        result.message.includes("JSON parse error: Cannot deserialize")
      ) {
        window.alert("Invalid age. Age must be a whole number");
      } else {
        window.alert(
          `An error occurred while processing your request.\n ${result.message}`
        );
      }
    }
  } catch (error) {
    console.error("Error:", error);
  }
}
