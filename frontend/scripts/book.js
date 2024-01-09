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
  slotsBody.innerHTML = ""; // Clear previous content
  if (slots.length === undefined || slots.length === 0) {
    slotsBody.innerHTML =
      "<tr><td colspan='8'>No slots available for the given vaccine.</td></tr>";
    return;
  }

  slots.forEach((slot) => {
    const row = document.createElement("tr");

    // Static columns
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

    // Add a button in the last column to show details
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

  // Display all data dynamically
  Object.entries(slot).forEach(([key, value]) => {
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
  const overlay = document.createElement("div");
  overlay.classList.add("overlay");

  const bookAppointmentFormContainer = document.createElement("div");
  bookAppointmentFormContainer.classList.add("details-container");

  // Display form fields dynamically
  const formFields = [
    {
      label: "Booking For:",
      inputType: "select",
      options: ["Self", "Other"],
      id: "bookingFor",
    },
    {
      label: "Dose Number:",
      inputType: "select",
      options: ["First", "Second", "Third", "Booster"],
      id: "doseNumber",
    },
    {
      label: "Gender:",
      inputType: "select",
      options: ["Male", "Female", "Transgender"],
      id: "gender",
    },
    { label: "Age:", inputType: "text", id: "age", required: true },
    { label: "Email:", inputType: "text", id: "email", required: true },
    {
      label: "First Name:",
      inputType: "text",
      id: "firstName",
      required: true,
    },
    { label: "Phone:", inputType: "text", id: "phone", required: true },
  ];

  let slotId = slot.slotId;
  const slotIdField = document.createElement("div");
  slotIdField.innerHTML = `<strong>Slot ID:</strong> ${slotId}`;
  bookAppointmentFormContainer.appendChild(slotIdField);

  let hospitalId = slot.doctorHospitalHospitalId;
  const hospitalIdField = document.createElement("div");
  hospitalIdField.innerHTML = `<strong>Hospital ID:</strong> ${hospitalId}`;
  bookAppointmentFormContainer.appendChild(hospitalIdField);

  const formData = {};

  formFields.forEach((field) => {
    const formItem = document.createElement("div");
    formItem.innerHTML = `<strong>${field.label}</strong>`;

    if (field.inputType === "select") {
      const select = document.createElement("select");
      select.id = field.id;
      field.options.forEach((option) => {
        const optionElement = document.createElement("option");
        optionElement.value = option.toLowerCase();
        optionElement.textContent = option;
        select.appendChild(optionElement);
      });
      formItem.appendChild(select);

      // Store the initial value in formData object
      formData[field.id] = field.options[0].toLowerCase();

      // Update formData when the select value changes
      select.addEventListener("change", (event) => {
        formData[field.id] = event.target.value;
      });
    } else if (field.inputType === "text") {
      const input = document.createElement("input");
      input.type = field.inputType;
      input.id = field.id;
      formItem.appendChild(input);

      // Store the initial value in formData object
      formData[field.id] = "";

      // Update formData when the input value changes
      input.addEventListener("input", (event) => {
        formData[field.id] = event.target.value;
      });
    }

    bookAppointmentFormContainer.appendChild(formItem);
  });

  const bookBtn = document.createElement("button");
  bookBtn.textContent = "Confirm";
  bookBtn.style.backgroundColor = "green";
  bookBtn.addEventListener("click", () => {
    // Call the bookAppointment function with form data
    bookAppointment(slotId, hospitalId, formData);
    // Close the overlay and form container
    overlay.remove();
    bookAppointmentFormContainer.remove();
  });
  bookAppointmentFormContainer.appendChild(bookBtn);

  const closeBtn = document.createElement("button");
  closeBtn.textContent = "Cancel";
  closeBtn.style.backgroundColor = "red";
  closeBtn.addEventListener("click", () => {
    overlay.remove();
    bookAppointmentFormContainer.remove();
  });
  bookAppointmentFormContainer.appendChild(closeBtn);

  overlay.appendChild(bookAppointmentFormContainer);
  document.body.appendChild(overlay);
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
