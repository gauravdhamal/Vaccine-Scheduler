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
  }
}

function displaySlots(slots) {
  const slotsContainer = document.getElementById("slotsContainer");
  slotsContainer.innerHTML = ""; // Clear previous content

  const table = document.createElement("table");
  const headerRow = table.insertRow();
  const columnsToDisplay = [
    "slotId",
    "slotDate",
    "slotTiming",
    "doctorHospitalHospitalId",
  ];

  columnsToDisplay.forEach((column) => {
    const th = document.createElement("th");
    th.textContent = column;
    headerRow.appendChild(th);
  });

  slots.forEach((slot) => {
    const row = table.insertRow();

    columnsToDisplay.forEach((column) => {
      const cell = row.insertCell();
      cell.textContent = slot[column];
    });

    // Add a button in the last column to show details
    const showDetailsButtonCell = row.insertCell();
    const showDetailsButton = document.createElement("button");
    showDetailsButton.textContent = "Show Details";
    showDetailsButton.addEventListener("click", () => showDetails(slot));
    showDetailsButtonCell.appendChild(showDetailsButton);
  });

  slotsContainer.appendChild(table);
}

function showDetails(slot) {
  const overlay = document.createElement("div");
  overlay.classList.add("overlay");

  const detailsContainer = document.createElement("div");
  detailsContainer.classList.add("details-container");

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
