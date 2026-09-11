const photos = {
  pier: "linear-gradient(135deg,#4fc3f7,#0277bd)",
  canyon: "linear-gradient(135deg,#ffb74d,#e65100 60%,#5d4037)",
  inNOut: "linear-gradient(135deg,#ffcc80,#d84315)",
  flagstaff: "linear-gradient(135deg,#81c784,#2e7d32)",
  cadillac: "linear-gradient(135deg,#ce93d8,#6a1b9a)",
  amarillo: "linear-gradient(135deg,#fff59d,#f9a825)",
  nashville: "linear-gradient(135deg,#90caf9,#1565c0)",
  bbq: "linear-gradient(135deg,#ffab91,#bf360c)",
  dc: "linear-gradient(135deg,#b0bec5,#455a64)",
};

const seedStops = [
  {
    id: "s1",
    placeName: "Santa Monica Pier",
    lat: 34.0101,
    lng: -118.4962,
    timestamp: "2026-09-01T08:12:00",
    notes: "Pacific start. Coffee on the pier before heading inland.",
    photo: photos.pier,
    x: 12,
    y: 58,
  },
  {
    id: "s2",
    placeName: "Flagstaff",
    lat: 35.1983,
    lng: -111.6513,
    timestamp: "2026-09-03T16:40:00",
    notes: "Cool evening after the desert. Overnight near downtown.",
    photo: photos.flagstaff,
    x: 28,
    y: 50,
  },
  {
    id: "s3",
    placeName: "Amarillo",
    lat: 35.222,
    lng: -101.8313,
    timestamp: "2026-09-05T13:15:00",
    notes: "Long I-40 day. Wind and wide sky.",
    photo: photos.amarillo,
    x: 44,
    y: 52,
  },
  {
    id: "s4",
    placeName: "Nashville",
    lat: 36.1627,
    lng: -86.7816,
    timestamp: "2026-09-08T18:05:00",
    notes: "Live music on Broadway. First real rain of the trip.",
    photo: photos.nashville,
    x: 68,
    y: 48,
  },
  {
    id: "s5",
    placeName: "Washington, D.C.",
    lat: 38.9072,
    lng: -77.0369,
    timestamp: "2026-09-11T11:20:00",
    notes: "Atlantic side. Trip still open — no end date yet.",
    photo: photos.dc,
    x: 86,
    y: 38,
  },
];

const seedHighlights = [
  {
    id: "h1",
    name: "In-N-Out, Barstow",
    category: "Food",
    rating: 4,
    lat: 34.8958,
    lng: -117.0173,
    timestamp: "2026-09-01T13:05:00",
    notes: "First official road-food stop. Animal style.",
    photo: photos.inNOut,
    x: 16,
    y: 54,
  },
  {
    id: "h2",
    name: "Grand Canyon South Rim",
    category: "Scenic",
    rating: 5,
    lat: 36.0544,
    lng: -112.1401,
    timestamp: "2026-09-02T10:30:00",
    notes: "Worth the extra hours. Late morning light on the inner gorge.",
    photo: photos.canyon,
    x: 26,
    y: 44,
  },
  {
    id: "h3",
    name: "Cadillac Ranch",
    category: "Landmark",
    rating: 5,
    lat: 35.1872,
    lng: -101.9871,
    timestamp: "2026-09-05T15:40:00",
    notes: "Spray paint optional. Kids loved it.",
    photo: photos.cadillac,
    x: 42,
    y: 56,
  },
  {
    id: "h4",
    name: "Music City BBQ",
    category: "Food",
    rating: 5,
    lat: 36.1627,
    lng: -86.7816,
    timestamp: "2026-09-08T19:20:00",
    notes: "Brisket after a wet walk down Broadway.",
    photo: photos.bbq,
    x: 70,
    y: 54,
  },
];

const seedPnwStops = [
  {
    id: "p1",
    placeName: "Pike Place Market",
    lat: 47.6097,
    lng: -122.3425,
    timestamp: "2026-07-12T09:10:00",
    notes: "Coffee and the first hill of the week.",
    photo: photos.nashville,
    x: 22,
    y: 28,
  },
  {
    id: "p2",
    placeName: "Cannon Beach",
    lat: 45.8918,
    lng: -123.9615,
    timestamp: "2026-07-16T17:45:00",
    notes: "Haystack at low tide. Overnight in town.",
    photo: photos.pier,
    x: 18,
    y: 48,
  },
  {
    id: "p3",
    placeName: "Portland",
    lat: 45.5152,
    lng: -122.6784,
    timestamp: "2026-07-18T12:05:00",
    notes: "Loop closed. Food carts before the airport.",
    photo: photos.bbq,
    x: 28,
    y: 52,
  },
];

const seedPnwHighlights = [
  {
    id: "ph1",
    name: "Hoh Rain Forest",
    category: "Scenic",
    rating: 5,
    lat: 47.8606,
    lng: -123.9347,
    timestamp: "2026-07-14T11:20:00",
    notes: "Moss so thick the trail felt indoor.",
    photo: photos.flagstaff,
    x: 14,
    y: 36,
  },
];

function createSeedTrips() {
  return [
    {
      id: "t1",
      title: "Coast to Coast 2026",
      startDate: "2026-09-01",
      endDate: null,
      notes: "USA coast-to-coast road trip",
      stops: structuredClone(seedStops),
      highlights: structuredClone(seedHighlights),
    },
    {
      id: "t2",
      title: "Pacific Northwest Loop",
      startDate: "2026-07-12",
      endDate: "2026-07-18",
      notes: "Seattle to Portland via the Olympics",
      stops: structuredClone(seedPnwStops),
      highlights: structuredClone(seedPnwHighlights),
    },
    {
      id: "t3",
      title: "Utah Parks",
      startDate: "2026-10-03",
      endDate: null,
      notes: "Planned — nothing logged yet.",
      stops: [],
      highlights: [],
    },
  ];
}

const categoryMeta = {
  Scenic: { icon: "eco", tint: "scenic", cls: "cat-scenic" },
  Food: { icon: "restaurant", tint: "food", cls: "cat-food" },
  Lodging: { icon: "hotel", tint: "lodging", cls: "cat-lodging" },
  Landmark: { icon: "star", tint: "landmark", cls: "cat-landmark" },
  Other: { icon: "place", tint: "other", cls: "cat-other" },
};

const state = {
  theme: "light",
  emptyList: false,
  units: "mi",
  tab: "home",
  screen: "trips",
  fabOpen: false,
  mapFilter: "All",
  timelineFilter: "All",
  timelineQuery: "",
  tripQuery: "",
  selectedStopId: null,
  selectedHighlightId: null,
  sheetId: null,
  sheetKind: null,
  dialog: null,
  pendingDeleteTripId: null,
  snack: null,
  photo: null,
  locationGranted: false,
  stopForm: blankStopForm(),
  highlightForm: blankHighlightForm(),
  tripForm: blankTripForm(),
  trips: createSeedTrips(),
  currentTripId: null,
};

function blankTripForm() {
  return {
    title: "",
    startDate: "2026-09-11",
    endDate: "",
    notes: "",
    dirty: false,
    editingId: null,
  };
}

function blankStopForm() {
  return {
    placeName: "",
    notes: "",
    lat: null,
    lng: null,
    photos: [],
    dirty: false,
  };
}

function blankHighlightForm() {
  return {
    name: "",
    category: "Scenic",
    rating: 3,
    notes: "",
    lat: null,
    lng: null,
    photos: [],
    dirty: false,
  };
}

const $app = () => document.getElementById("app");

function icon(name, filled = false) {
  return `<span class="material-symbols-outlined${filled ? " fill" : ""}">${name}</span>`;
}

function fmtTime(iso) {
  return new Date(iso).toLocaleString(undefined, {
    month: "short",
    day: "numeric",
    hour: "numeric",
    minute: "2-digit",
  });
}

function fmtDay(iso) {
  return new Date(iso).toLocaleDateString(undefined, {
    weekday: "long",
    month: "short",
    day: "numeric",
  });
}

function fmtCoord(lat, lng) {
  return `${lat.toFixed(4)}, ${lng.toFixed(4)}`;
}

function currentTrip() {
  return state.trips.find((trip) => trip.id === state.currentTripId) || null;
}

function tripStops() {
  return currentTrip()?.stops ?? [];
}

function tripHighlights() {
  return currentTrip()?.highlights ?? [];
}

function tripDistanceMiles(trip) {
  const sorted = [...(trip?.stops ?? [])].sort((a, b) => a.timestamp.localeCompare(b.timestamp));
  if (sorted.length < 2) return 0;
  let total = 0;
  for (let i = 1; i < sorted.length; i += 1) {
    total += haversine(sorted[i - 1], sorted[i]);
  }
  return total;
}

function formatDistance(milesValue) {
  if (state.units === "km") {
    return { value: Math.round(milesValue * 1.60934), label: "Kilometers" };
  }
  return { value: Math.round(milesValue), label: "Miles" };
}

function formatTripDates(trip) {
  const start = new Date(`${trip.startDate}T12:00:00`).toLocaleDateString(undefined, {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
  if (!trip.endDate) return `${start} · Open`;
  const end = new Date(`${trip.endDate}T12:00:00`).toLocaleDateString(undefined, {
    month: "short",
    day: "numeric",
  });
  return `${start} – ${end}`;
}

function haversine(a, b) {
  const r = 3958.8;
  const dLat = ((b.lat - a.lat) * Math.PI) / 180;
  const dLng = ((b.lng - a.lng) * Math.PI) / 180;
  const lat1 = (a.lat * Math.PI) / 180;
  const lat2 = (b.lat * Math.PI) / 180;
  const h =
    Math.sin(dLat / 2) ** 2 +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLng / 2) ** 2;
  return 2 * r * Math.asin(Math.sqrt(h));
}

function activityItems() {
  const stops = tripStops().map((s) => ({
    kind: "stop",
    id: s.id,
    title: s.placeName || "Stop",
    timestamp: s.timestamp,
    photo: s.photo,
    icon: "location_on",
    tint: "stop",
  }));
  const highlights = tripHighlights().map((h) => ({
    kind: "highlight",
    id: h.id,
    title: h.name,
    timestamp: h.timestamp,
    photo: h.photo,
    icon: categoryMeta[h.category].icon,
    tint: categoryMeta[h.category].tint,
  }));
  return [...stops, ...highlights].sort((a, b) => b.timestamp.localeCompare(a.timestamp));
}

function render() {
  document.getElementById("device").dataset.theme = state.theme;
  document.getElementById("toggleTheme").textContent =
    state.theme === "dark" ? "Light theme" : "Dark theme";
  document.getElementById("toggleTheme").setAttribute("aria-pressed", String(state.theme === "dark"));
  document.getElementById("toggleEmpty").setAttribute("aria-pressed", String(state.emptyList));
  $app().innerHTML = screenHtml() + overlaysHtml();
  bindStatic();
}

function screenHtml() {
  switch (state.screen) {
    case "trips":
      return tripsHtml();
    case "createTrip":
      return createTripHtml();
    case "addStop":
      return addStopHtml();
    case "addHighlight":
      return addHighlightHtml();
    case "stopDetail":
      return stopDetailHtml();
    case "highlightDetail":
      return highlightDetailHtml();
    case "settings":
      return settingsHtml();
    default:
      return tabShell();
  }
}

function tabShell() {
  const body =
    state.tab === "home" ? homeHtml() : state.tab === "map" ? mapHtml() : timelineHtml();
  const showFab = (state.tab === "home" || state.tab === "map") && !state.sheetId;
  return `
    ${body}
    ${showFab ? fabHtml() : ""}
    ${navHtml()}
  `;
}

function navHtml() {
  const items = [
    ["home", "home", "Home"],
    ["map", "map", "Map"],
    ["timeline", "schedule", "Timeline"],
  ];
  return `
    <nav class="nav-bar" aria-label="Main">
      ${items
        .map(
          ([id, glyph, label]) => `
        <button class="nav-item ${state.tab === id ? "active" : ""}" data-tab="${id}">
          <span class="nav-icon">${icon(glyph, state.tab === id)}</span>
          ${label}
        </button>`
        )
        .join("")}
    </nav>`;
}

function fabHtml() {
  return `
    <button class="fab ${state.fabOpen ? "open" : ""}" id="fab" aria-expanded="${state.fabOpen}" aria-label="Add">
      ${icon(state.fabOpen ? "close" : "add")}
    </button>`;
}

function tripsHtml() {
  const query = state.tripQuery.toLowerCase();
  const trips = state.trips
    .filter((trip) => `${trip.title} ${trip.notes}`.toLowerCase().includes(query))
    .sort((a, b) => b.startDate.localeCompare(a.startDate));
  return `
    <section class="screen">
      <div class="app-bar">
        <div style="width:48px"></div>
        <h1>Trips</h1>
        <button class="icon-btn" data-go="settings" aria-label="Settings">${icon("settings")}</button>
      </div>
      <div class="search-field field">
        <input id="tripSearch" type="search" placeholder="Search trips…" value="${escapeHtml(state.tripQuery)}" />
      </div>
      <div class="scroll">
        ${
          state.trips.length === 0
            ? `<div class="empty">
                ${icon("map")}
                <h3>No trips yet</h3>
                <p>Start a trip, then log stops as you drive. Everything stays on this device.</p>
                <button class="filled" data-create-trip>Start a trip</button>
              </div>`
            : trips.length === 0
              ? `<div class="empty"><h3>No matches</h3><p>Try another search.</p></div>`
              : trips.map(tripCard).join("")
        }
      </div>
      ${
        state.trips.length
          ? `<button class="fab list-fab" data-create-trip aria-label="Start a trip">${icon("add")}</button>`
          : ""
      }
    </section>`;
}

function tripCard(trip) {
  const distance = formatDistance(tripDistanceMiles(trip));
  return `
    <article class="trip-card">
      <div class="trip-card-top">
        <button class="trip-open" data-open-trip="${trip.id}">
          <strong>${escapeHtml(trip.title)}</strong>
          <span>${formatTripDates(trip)}</span>
        </button>
        <button class="icon-btn" data-trip-overflow="${trip.id}" aria-label="Trip options">${icon("more_vert")}</button>
      </div>
      <p class="trip-meta">${trip.stops.length} stops · ${trip.highlights.length} highlights · ${distance.value} ${distance.label.toLowerCase()}</p>
    </article>`;
}

function createTripHtml() {
  const f = state.tripForm;
  const canSave = f.title.trim().length > 0;
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-close-trip-form aria-label="Close">${icon("close")}</button>
        <h1>${f.editingId ? "Edit trip" : "Start a trip"}</h1>
        <button class="text-btn" data-save-trip ${canSave ? "" : "disabled"}>Save</button>
      </div>
      <div class="scroll tight">
        <div class="form">
          <div class="field">
            <label for="newTripTitle">Trip name</label>
            <input id="newTripTitle" value="${escapeHtml(f.title)}" placeholder="Required" />
          </div>
          <div class="field">
            <label for="newTripStart">Start date</label>
            <input id="newTripStart" type="date" value="${escapeHtml(f.startDate)}" />
          </div>
          <div class="field">
            <label for="newTripEnd">End date</label>
            <input id="newTripEnd" type="date" value="${escapeHtml(f.endDate)}" />
          </div>
          <div class="field">
            <label for="newTripNotes">Notes</label>
            <textarea id="newTripNotes" placeholder="Optional">${escapeHtml(f.notes)}</textarea>
          </div>
        </div>
      </div>
    </section>`;
}

function homeHtml() {
  const trip = currentTrip();
  if (!trip) return tripsHtml();
  const empty = trip.stops.length === 0 && trip.highlights.length === 0;
  const recent = activityItems().slice(0, 5);
  const distance = formatDistance(tripDistanceMiles(trip));
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-back-trips aria-label="All trips">${icon("arrow_back")}</button>
        <h1>Trip</h1>
        <button class="icon-btn" data-go="settings" aria-label="Settings">${icon("settings")}</button>
      </div>
      <div class="hero">
        <h2>${escapeHtml(trip.title)}</h2>
        <p class="kicker">${formatTripDates(trip)}</p>
      </div>
      <div class="scroll">
        <div class="stats">
          ${statCard("location_on", trip.stops.length, "Stops")}
          ${statCard("star", trip.highlights.length, "Highlights")}
          ${statCard("photo", activityItems().length, "Photos")}
          ${statCard("route", empty ? 0 : distance.value, distance.label)}
        </div>
        ${
          empty
            ? emptyState(
                "map",
                "No stops yet",
                "Log a stop or highlight as you drive. Everything stays on this device.",
                "Log stop"
              )
            : `<h3 class="section-title">Recent activity</h3>
               ${recent.map(activityRow).join("")}`
        }
      </div>
    </section>`;
}

function statCard(glyph, value, label) {
  return `
    <article class="stat">
      ${icon(glyph, true)}
      <span class="value">${value}</span>
      <span class="label">${label}</span>
    </article>`;
}

function activityRow(item) {
  return `
    <button class="list-row" data-open="${item.kind}:${item.id}">
      <span class="leading-icon tint-${item.tint}">${icon(item.icon, true)}</span>
      <span class="row-copy">
        <strong>${escapeHtml(item.title)}</strong>
        <span>${fmtTime(item.timestamp)}</span>
      </span>
      <span class="thumb" style="background:${item.photo}"></span>
    </button>`;
}

function emptyState(glyph, title, copy, action) {
  return `
    <div class="empty">
      ${icon(glyph)}
      <h3>${title}</h3>
      <p>${copy}</p>
      <button class="filled" data-add="stop">${action}</button>
    </div>`;
}

function mapHtml() {
  const chips = ["All", "Stops", "Highlights", "Scenic", "Food", "Landmark"];
  const showStops = state.mapFilter === "All" || state.mapFilter === "Stops";
  const showHighlights =
    state.mapFilter === "All" ||
    state.mapFilter === "Highlights" ||
    ["Scenic", "Food", "Lodging", "Landmark", "Other"].includes(state.mapFilter);

  const stopPins = tripStops()
    .filter(() => showStops)
    .map(
      (s) => `
      <button class="pin stop" style="left:${s.x}%;top:${s.y}%" data-sheet="stop:${s.id}" aria-label="${escapeHtml(s.placeName)}">
        ${icon("location_on", true)}
      </button>`
    )
    .join("");

  const highlightPins = tripHighlights()
    .filter((h) => showHighlights && (state.mapFilter === "All" || state.mapFilter === "Highlights" || h.category === state.mapFilter))
    .map((h) => {
      const meta = categoryMeta[h.category];
      return `
        <button class="pin highlight" style="left:${h.x}%;top:${h.y}%" data-sheet="highlight:${h.id}" aria-label="${escapeHtml(h.name)}">
          <span class="badge ${meta.cls}">${icon(meta.icon, true)}</span>
        </button>`;
    })
    .join("");

  const points = [...tripStops()].sort((a, b) => a.timestamp.localeCompare(b.timestamp));
  const path = points.map((p, i) => `${i === 0 ? "M" : "L"} ${p.x} ${p.y}`).join(" ");

  return `
    <section class="screen map-screen">
      <div class="app-bar">
        <div style="width:48px"></div>
        <h1>Map</h1>
        <div style="width:48px"></div>
      </div>
      <div class="chips" style="position:absolute;top:56px;left:0;right:0;z-index:3">
        ${chips
          .map(
            (c) => `<button class="chip ${state.mapFilter === c ? "active" : ""}" data-map-filter="${c}">${c}</button>`
          )
          .join("")}
      </div>
      <div class="map-canvas">
        <svg class="route" viewBox="0 0 100 100" preserveAspectRatio="none" aria-hidden="true">
          <path d="${path}" fill="none" stroke="#3A72FB" stroke-width="1.2" stroke-linecap="round" />
        </svg>
        ${stopPins}
        ${highlightPins}
        <div class="map-tools">
          <button type="button" data-my-location aria-label="My location">${icon("my_location")}</button>
          <button type="button" aria-label="Fit route">${icon("zoom_out_map")}</button>
        </div>
      </div>
      ${sheetHtml()}
    </section>`;
}

function sheetHtml() {
  if (!state.sheetId) return "";
  const item =
    state.sheetKind === "stop"
      ? tripStops().find((s) => s.id === state.sheetId)
      : tripHighlights().find((h) => h.id === state.sheetId);
  if (!item) return "";
  const title = item.placeName || item.name;
  const support =
    state.sheetKind === "stop"
      ? fmtTime(item.timestamp)
      : `${item.category} · ${"★".repeat(item.rating)}`;
  return `
    <aside class="sheet" role="dialog" aria-label="${escapeHtml(title)}">
      <div class="handle"></div>
      <div style="display:flex;gap:12px;align-items:center">
        <div class="thumb lg" style="background:${item.photo}"></div>
        <div class="row-copy">
          <strong>${escapeHtml(title)}</strong>
          <span>${support}</span>
        </div>
      </div>
      <div class="sheet-actions">
        <button class="filled" data-open="${state.sheetKind}:${item.id}">Open</button>
        <button class="outline" data-directions="${item.lat},${item.lng}">Directions</button>
      </div>
    </aside>`;
}

function timelineHtml() {
  const empty = activityItems().length === 0;
  const q = state.timelineQuery.toLowerCase();
  let items = activityItems().map((item) => {
    if (item.kind === "stop") {
      const stop = tripStops().find((s) => s.id === item.id);
      return { ...item, subtitle: fmtCoord(stop.lat, stop.lng), notes: stop.notes };
    }
    const highlight = tripHighlights().find((h) => h.id === item.id);
    return { ...item, subtitle: highlight.category, notes: highlight.notes };
  });
  if (state.timelineFilter === "Stops") items = items.filter((i) => i.kind === "stop");
  if (state.timelineFilter === "Highlights") items = items.filter((i) => i.kind === "highlight");
  if (q) {
    items = items.filter((i) =>
      `${i.title} ${i.subtitle} ${i.notes}`.toLowerCase().includes(q)
    );
  }
  const groups = groupByDay(items);

  return `
    <section class="screen">
      <div class="app-bar">
        <div style="width:48px"></div>
        <h1>Timeline</h1>
        <div style="width:48px"></div>
      </div>
      <div class="search-field field">
        <input id="timelineSearch" type="search" placeholder="Search places, notes, food…" value="${escapeHtml(state.timelineQuery)}" />
      </div>
      <div class="chips">
        ${["All", "Stops", "Highlights"]
          .map(
            (c) =>
              `<button class="chip ${state.timelineFilter === c ? "active" : ""}" data-tl-filter="${c}">${c}</button>`
          )
          .join("")}
      </div>
      <div class="scroll">
        ${
          empty || items.length === 0
            ? emptyState(
                "schedule",
                empty ? "No entries yet" : "No matches",
                empty
                  ? "Stops and highlights show up here by day."
                  : "Try another search or filter.",
                "Log stop"
              )
            : groups
                .map(
                  ([day, rows]) => `
            <section class="day-group">
              <div class="day-label">${day}</div>
              ${rows
                .map(
                  (item) => `
                <button class="list-row flat" data-open="${item.kind}:${item.id}">
                  <span class="leading-icon tint-${item.tint}">${icon(item.icon, true)}</span>
                  <span class="row-copy">
                    <strong>${escapeHtml(item.title)}</strong>
                    <span>${escapeHtml(item.subtitle)}</span>
                    <span>${new Date(item.timestamp).toLocaleTimeString(undefined, { hour: "numeric", minute: "2-digit" })}</span>
                  </span>
                  <span class="thumb lg" style="background:${item.photo}"></span>
                </button>`
                )
                .join("")}
            </section>`
                )
                .join("")
        }
      </div>
    </section>`;
}

function groupByDay(items) {
  const map = new Map();
  items.forEach((item) => {
    const key = fmtDay(item.timestamp);
    if (!map.has(key)) map.set(key, []);
    map.get(key).push(item);
  });
  return [...map.entries()];
}

function addStopHtml() {
  const f = state.stopForm;
  const canSave = f.lat != null && f.lng != null;
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-close-form aria-label="Close">${icon("close")}</button>
        <h1>${f.editingId ? "Edit stop" : "Log stop"}</h1>
        <button class="text-btn" data-save-stop ${canSave ? "" : "disabled"}>Save</button>
      </div>
      <div class="scroll tight">
        <div class="form">
          <section class="card">
            <h3 class="section-title">Location</h3>
            ${
              state.locationGranted && f.lat != null
                ? `<p class="fix">${icon("my_location")} ${fmtCoord(f.lat, f.lng)}</p>`
                : `<div class="banner">Location is off. Tap the map, or enable location.
                    <button class="text-btn" data-ask-location>Enable</button></div>`
            }
            <div class="actions-row" style="margin:10px 0">
              <button class="filled" data-use-location>Use current location</button>
            </div>
            <div class="field">
              <label for="placeName">Place name</label>
              <input id="placeName" value="${escapeHtml(f.placeName)}" placeholder="Optional" />
            </div>
            <button class="mini-map" data-drop-pin aria-label="Tap map to drop pin">
              ${f.lat != null ? `<span class="dropped-pin">${icon("location_on", true)}</span>` : ""}
            </button>
            <p class="muted">Tap the map to adjust the pin.</p>
          </section>
          <div class="field">
            <label for="stopNotes">Notes</label>
            <textarea id="stopNotes" placeholder="What happened here?">${escapeHtml(f.notes)}</textarea>
          </div>
          <section>
            <h3 class="section-title">Photos</h3>
            <div class="actions-row">
              <button class="outline" data-add-photo="stop">Choose photos</button>
              <button class="tonal" data-take-photo="stop">Take photo</button>
            </div>
            <div class="photo-row" style="margin-top:10px">
              ${f.photos
                .map(
                  (p, i) =>
                    `<div class="photo-tile" style="background:${p}"><button data-remove-photo="stop:${i}" aria-label="Remove">${icon("close")}</button></div>`
                )
                .join("")}
            </div>
          </section>
        </div>
      </div>
    </section>`;
}

function addHighlightHtml() {
  const f = state.highlightForm;
  const canSave = f.name.trim() && f.lat != null && f.lng != null;
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-close-form aria-label="Close">${icon("close")}</button>
        <h1>${f.editingId ? "Edit highlight" : "Add highlight"}</h1>
        <button class="text-btn" data-save-highlight ${canSave ? "" : "disabled"}>Save</button>
      </div>
      <div class="scroll tight">
        <div class="form">
          <div class="field">
            <label for="hlName">Name</label>
            <input id="hlName" value="${escapeHtml(f.name)}" placeholder="Required" />
          </div>
          <div>
            <p class="section-title">Category</p>
            <div class="chips" style="padding-left:0">
              ${Object.keys(categoryMeta)
                .map(
                  (c) =>
                    `<button class="chip ${f.category === c ? "active" : ""}" data-hl-cat="${c}">${c}</button>`
                )
                .join("")}
            </div>
          </div>
          <div>
            <p class="section-title">Rating</p>
            <div class="stars">
              ${[1, 2, 3, 4, 5]
                .map(
                  (n) =>
                    `<button data-hl-star="${n}" aria-label="${n} stars">${icon(n <= f.rating ? "star" : "star", n <= f.rating)}</button>`
                )
                .join("")}
            </div>
          </div>
          <section class="card">
            <h3 class="section-title">Location</h3>
            ${
              f.lat != null
                ? `<p class="fix">${icon("my_location")} ${fmtCoord(f.lat, f.lng)}</p>`
                : `<div class="banner">Drop a pin to save this highlight.
                    <button class="text-btn" data-ask-location>Enable</button></div>`
            }
            <div class="actions-row" style="margin:10px 0">
              <button class="filled" data-use-location>Use current location</button>
            </div>
            <button class="mini-map" data-drop-pin aria-label="Tap map to drop pin">
              ${f.lat != null ? `<span class="dropped-pin">${icon("location_on", true)}</span>` : ""}
            </button>
          </section>
          <div class="field">
            <label for="hlNotes">Notes</label>
            <textarea id="hlNotes" placeholder="Why is this place special?">${escapeHtml(f.notes)}</textarea>
          </div>
          <div class="actions-row">
            <button class="outline" data-add-photo="highlight">Choose photos</button>
            <button class="tonal" data-take-photo="highlight">Take photo</button>
          </div>
          <div class="photo-row">
            ${f.photos
              .map(
                (p, i) =>
                  `<div class="photo-tile" style="background:${p}"><button data-remove-photo="highlight:${i}" aria-label="Remove">${icon("close")}</button></div>`
              )
              .join("")}
          </div>
        </div>
      </div>
    </section>`;
}

function stopDetailHtml() {
  const stop = tripStops().find((s) => s.id === state.selectedStopId);
  if (!stop) return homeHtml();
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-back aria-label="Back">${icon("arrow_back")}</button>
        <h1>Stop</h1>
        <button class="icon-btn" data-dialog="overflow-stop" aria-label="More">${icon("more_vert")}</button>
      </div>
      <div class="scroll tight" style="padding-left:0;padding-right:0">
        <div class="mini-map hero-map">
          <span class="dropped-pin">${icon("location_on", true)}</span>
        </div>
        <div class="meta-block">
          <h2>${escapeHtml(stop.placeName || "Stop")}</h2>
          <p class="muted">${fmtTime(stop.timestamp)}</p>
          <p class="muted">${fmtCoord(stop.lat, stop.lng)}</p>
        </div>
        ${
          stop.notes
            ? `<div class="notes"><h3>Notes</h3><p>${escapeHtml(stop.notes)}</p></div>`
            : ""
        }
        <h3 class="section-title" style="padding:16px 20px 0">Photos</h3>
        <div class="grid-photos">
          <button class="photo" style="background:${stop.photo}" data-photo="${stop.photo}|${escapeHtml(stop.placeName)}"></button>
        </div>
        <div style="padding:0 20px 24px">
          <button class="outline" data-dialog="delete-stop">Delete stop</button>
        </div>
      </div>
    </section>`;
}

function highlightDetailHtml() {
  const highlight = tripHighlights().find((h) => h.id === state.selectedHighlightId);
  if (!highlight) return homeHtml();
  const meta = categoryMeta[highlight.category];
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-back aria-label="Back">${icon("arrow_back")}</button>
        <h1>Highlight</h1>
        <button class="icon-btn" data-dialog="overflow-highlight" aria-label="More">${icon("more_vert")}</button>
      </div>
      <div class="scroll tight" style="padding-left:0;padding-right:0">
        <div class="mini-map hero-map">
          <span class="dropped-pin" style="color:white">
            <span class="badge ${meta.cls}" style="width:36px;height:36px;border-radius:18px;display:grid;place-items:center">${icon(meta.icon, true)}</span>
          </span>
        </div>
        <div class="meta-block">
          <p class="muted">${highlight.category}</p>
          <h2>${escapeHtml(highlight.name)}</h2>
          <div class="stars" aria-label="${highlight.rating} stars">
            ${[1, 2, 3, 4, 5]
              .map((n) => icon(n <= highlight.rating ? "star" : "star", n <= highlight.rating))
              .join("")}
          </div>
          <p class="muted">${fmtTime(highlight.timestamp)}</p>
        </div>
        ${
          highlight.notes
            ? `<div class="notes"><h3>Notes</h3><p>${escapeHtml(highlight.notes)}</p></div>`
            : ""
        }
        <h3 class="section-title" style="padding:16px 20px 0">Photos</h3>
        <div class="grid-photos">
          <button class="photo" style="background:${highlight.photo}" data-photo="${highlight.photo}|${escapeHtml(highlight.name)}"></button>
        </div>
        <div style="padding:0 20px 24px">
          <button class="outline" data-dialog="delete-highlight">Delete highlight</button>
        </div>
      </div>
    </section>`;
}

function settingsHtml() {
  const trip = currentTrip();
  return `
    <section class="screen">
      <div class="app-bar">
        <button class="icon-btn" data-back aria-label="Back">${icon("arrow_back")}</button>
        <h1>Settings</h1>
      </div>
      <div class="scroll tight">
        <div class="form">
          ${
            trip
              ? `<div class="field">
                  <label for="tripTitle">Trip name</label>
                  <input id="tripTitle" value="${escapeHtml(trip.title)}" />
                </div>
                <div class="field">
                  <label for="tripNotes">Trip notes</label>
                  <textarea id="tripNotes">${escapeHtml(trip.notes)}</textarea>
                </div>`
              : ""
          }
          <div class="card">
            <p class="section-title">Distance</p>
            <div class="segmented" role="group" aria-label="Distance unit">
              <button class="${state.units === "mi" ? "active" : ""}" data-units="mi">Miles</button>
              <button class="${state.units === "km" ? "active" : ""}" data-units="km">Kilometers</button>
            </div>
            <p class="muted" style="margin-top:8px">Same stop-to-stop formula as iOS. Highlights do not add distance.</p>
          </div>
          <div class="card">
            <p class="section-title">Appearance</p>
            <p class="muted">Use the prototype toolbar for light/dark. The app will honor system theme and Material You dynamic color.</p>
          </div>
          <div class="card">
            <p class="section-title">Maps</p>
            <p class="muted">Google Maps for the route canvas. Directions opens the Google Maps app with this pin.</p>
          </div>
          <div class="card">
            <p class="section-title">Privacy</p>
            <p class="muted">All trip data stays on this device. CoastTrip does not create an account or upload photos.</p>
          </div>
        </div>
      </div>
    </section>`;
}

function overlaysHtml() {
  let html = "";
  if (state.fabOpen) {
    html += `
      <div class="scrim" data-close-fab></div>
      <div class="fab-menu">
        <button class="fab-choice" data-add="stop">
          <span class="label">Log stop</span>
          <span class="bubble">${icon("add_location")}</span>
        </button>
        <button class="fab-choice secondary" data-add="highlight">
          <span class="label">Add highlight</span>
          <span class="bubble">${icon("star")}</span>
        </button>
      </div>`;
  }
  if (state.dialog === "trip-overflow") {
    html += dialog(
      "Trip",
      "Rename this trip or remove it from the device.",
      `<button class="text-btn" data-edit-trip>Edit</button>
       <button class="text-btn danger" data-dialog="delete-trip">Delete</button>`
    );
  }
  if (state.dialog === "delete-trip") {
    html += dialog(
      "Delete this trip?",
      "Stops, highlights, and photos for this trip will be removed from the device.",
      `<button class="text-btn" data-dialog-cancel>Cancel</button>
       <button class="text-btn danger" data-confirm-delete-trip>Delete</button>`
    );
  }
  if (state.dialog === "location") {
    html += dialog(
      "Use your location?",
      "CoastTrip saves a pin for each stop. Location stays on this device.",
      `<button class="text-btn" data-dialog-cancel>Not now</button>
       <button class="filled" data-grant-location>Continue</button>`
    );
  }
  if (state.dialog === "overflow-stop" || state.dialog === "overflow-highlight") {
    const kind = state.dialog === "overflow-stop" ? "stop" : "highlight";
    html += dialog(
      kind === "stop" ? "Stop" : "Highlight",
      "Edit this entry or remove it from the trip.",
      `<button class="text-btn" data-edit="${kind}">Edit</button>
       <button class="text-btn danger" data-dialog="delete-${kind}">Delete</button>`
    );
  }
  if (state.dialog === "delete-stop" || state.dialog === "delete-highlight") {
    const kind = state.dialog === "delete-stop" ? "stop" : "highlight";
    html += dialog(
      `Delete this ${kind}?`,
      `Photos stored for this ${kind} will be removed from the device.`,
      `<button class="text-btn" data-dialog-cancel>Cancel</button>
       <button class="text-btn danger" data-confirm-delete="${kind}">Delete</button>`
    );
  }
  if (state.dialog === "discard") {
    html += dialog(
      "Discard this entry?",
      "Your pin, notes, and photos have not been saved.",
      `<button class="text-btn" data-dialog-cancel>Keep editing</button>
       <button class="text-btn danger" data-discard>Discard</button>`
    );
  }
  if (state.snack) {
    html += `<div class="snack"><span>${escapeHtml(state.snack.text)}</span>
      ${state.snack.action ? `<button class="text-btn" data-snack-action>${state.snack.action}</button>` : ""}
    </div>`;
  }
  if (state.photo) {
    const [bg, caption] = state.photo.split("|");
    html += `
      <div class="photo-viewer">
        <div class="app-bar">
          <button class="icon-btn" data-close-photo aria-label="Close">${icon("close")}</button>
          <h1>Photo</h1>
        </div>
        <div class="stage-photo" style="background:${bg}"></div>
        <div class="photo-caption">${escapeHtml(caption || "Add a caption")}</div>
      </div>`;
  }
  return html;
}

function dialog(title, body, actions) {
  return `
    <div class="dialog-wrap">
      <div class="dialog" role="alertdialog" aria-labelledby="dlgTitle">
        <h2 id="dlgTitle">${title}</h2>
        <p>${body}</p>
        <div class="dialog-actions">${actions}</div>
      </div>
    </div>`;
}

function overlaysAfterBind() {
  // handled in bindStatic
}

function bindStatic() {
  $app().querySelectorAll("[data-tab]").forEach((el) => {
    el.addEventListener("click", () => {
      state.tab = el.dataset.tab;
      state.screen = "home";
      state.fabOpen = false;
      state.sheetId = null;
      render();
    });
  });
  const fab = document.getElementById("fab");
  if (fab) {
    fab.addEventListener("click", () => {
      state.fabOpen = !state.fabOpen;
      render();
    });
  }
  $app().querySelectorAll("[data-close-fab]").forEach((el) => {
    el.addEventListener("click", () => {
      state.fabOpen = false;
      render();
    });
  });
  $app().querySelectorAll("[data-add]").forEach((el) => {
    el.addEventListener("click", () => {
      state.fabOpen = false;
      if (el.dataset.add === "stop") {
        state.stopForm = blankStopForm();
        state.screen = "addStop";
      } else {
        state.highlightForm = blankHighlightForm();
        state.screen = "addHighlight";
      }
      render();
    });
  });
  $app().querySelectorAll("[data-go]").forEach((el) => {
    el.addEventListener("click", () => {
      state.screen = el.dataset.go;
      render();
    });
  });
  $app().querySelectorAll("[data-back]").forEach((el) => {
    el.addEventListener("click", () => {
      if (state.screen === "settings" && !state.currentTripId) {
        state.screen = "trips";
      } else {
        state.screen = "home";
      }
      state.sheetId = null;
      render();
    });
  });
  $app().querySelectorAll("[data-open]").forEach((el) => {
    el.addEventListener("click", () => {
      const [kind, id] = el.dataset.open.split(":");
      if (kind === "stop") {
        state.selectedStopId = id;
        state.screen = "stopDetail";
      } else {
        state.selectedHighlightId = id;
        state.screen = "highlightDetail";
      }
      state.sheetId = null;
      state.fabOpen = false;
      render();
    });
  });
  $app().querySelectorAll("[data-sheet]").forEach((el) => {
    el.addEventListener("click", () => {
      const [kind, id] = el.dataset.sheet.split(":");
      state.sheetKind = kind;
      state.sheetId = id;
      render();
    });
  });
  $app().querySelectorAll("[data-map-filter]").forEach((el) => {
    el.addEventListener("click", () => {
      state.mapFilter = el.dataset.mapFilter;
      render();
    });
  });
  $app().querySelectorAll("[data-tl-filter]").forEach((el) => {
    el.addEventListener("click", () => {
      state.timelineFilter = el.dataset.tlFilter;
      render();
    });
  });
  const search = document.getElementById("timelineSearch");
  if (search) {
    search.addEventListener("input", () => {
      state.timelineQuery = search.value;
      render();
      const again = document.getElementById("timelineSearch");
      if (again) {
        again.focus();
        again.setSelectionRange(again.value.length, again.value.length);
      }
    });
  }
  $app().querySelectorAll("[data-close-form]").forEach((el) => {
    el.addEventListener("click", () => {
      const form = state.screen === "addStop" ? state.stopForm : state.highlightForm;
      if (form.dirty || form.lat != null || form.notes || form.placeName || form.name) {
        state.dialog = "discard";
        render();
        return;
      }
      state.screen = "home";
      render();
    });
  });
  $app().querySelectorAll("[data-ask-location],[data-use-location],[data-my-location]").forEach((el) => {
    el.addEventListener("click", () => {
      if (!state.locationGranted) {
        state.dialog = "location";
        render();
        return;
      }
      applyCurrentLocation();
    });
  });
  $app().querySelectorAll("[data-grant-location]").forEach((el) => {
    el.addEventListener("click", () => {
      state.locationGranted = true;
      state.dialog = null;
      applyCurrentLocation();
    });
  });
  $app().querySelectorAll("[data-drop-pin]").forEach((el) => {
    el.addEventListener("click", () => {
      applyCurrentLocation();
    });
  });
  const place = document.getElementById("placeName");
  if (place) {
    place.addEventListener("input", () => {
      state.stopForm.placeName = place.value;
      state.stopForm.dirty = true;
    });
  }
  const stopNotes = document.getElementById("stopNotes");
  if (stopNotes) {
    stopNotes.addEventListener("input", () => {
      state.stopForm.notes = stopNotes.value;
      state.stopForm.dirty = true;
    });
  }
  const hlName = document.getElementById("hlName");
  if (hlName) {
    hlName.addEventListener("input", () => {
      state.highlightForm.name = hlName.value;
      state.highlightForm.dirty = true;
      renderKeepHighlight();
    });
  }
  const hlNotes = document.getElementById("hlNotes");
  if (hlNotes) {
    hlNotes.addEventListener("input", () => {
      state.highlightForm.notes = hlNotes.value;
      state.highlightForm.dirty = true;
    });
  }
  $app().querySelectorAll("[data-hl-cat]").forEach((el) => {
    el.addEventListener("click", () => {
      state.highlightForm.category = el.dataset.hlCat;
      state.highlightForm.dirty = true;
      render();
    });
  });
  $app().querySelectorAll("[data-hl-star]").forEach((el) => {
    el.addEventListener("click", () => {
      state.highlightForm.rating = Number(el.dataset.hlStar);
      state.highlightForm.dirty = true;
      render();
    });
  });
  $app().querySelectorAll("[data-save-stop]").forEach((el) => {
    el.addEventListener("click", () => {
      if (state.stopForm.lat == null) return;
      const id = state.stopForm.editingId || `s${Date.now()}`;
      const next = {
        id,
        placeName: state.stopForm.placeName || "Current location",
        lat: state.stopForm.lat,
        lng: state.stopForm.lng,
        timestamp: new Date().toISOString(),
        notes: state.stopForm.notes,
        photo: state.stopForm.photos[0] || photos.flagstaff,
        x: 50,
        y: 46,
      };
      const existing = tripStops().find((s) => s.id === id);
      if (existing) {
        Object.assign(existing, next, { timestamp: existing.timestamp, x: existing.x, y: existing.y });
        state.selectedStopId = id;
        state.screen = "stopDetail";
      } else {
        currentTrip().stops.push(next);
        state.screen = "home";
        state.tab = "home";
      }
      state.snack = { text: existing ? "Stop updated" : "Stop saved", action: "View", id, kind: "stop" };
      render();
      window.setTimeout(() => {
        if (state.snack && state.snack.id === id) {
          state.snack = null;
          render();
        }
      }, 3200);
    });
  });
  $app().querySelectorAll("[data-save-highlight]").forEach((el) => {
    el.addEventListener("click", () => {
      if (!state.highlightForm.name.trim() || state.highlightForm.lat == null) return;
      const id = state.highlightForm.editingId || `h${Date.now()}`;
      const next = {
        id,
        name: state.highlightForm.name.trim(),
        category: state.highlightForm.category,
        rating: state.highlightForm.rating,
        lat: state.highlightForm.lat,
        lng: state.highlightForm.lng,
        timestamp: new Date().toISOString(),
        notes: state.highlightForm.notes,
        photo: state.highlightForm.photos[0] || photos.canyon,
        x: 54,
        y: 42,
      };
      const existing = tripHighlights().find((h) => h.id === id);
      if (existing) {
        Object.assign(existing, next, { timestamp: existing.timestamp, x: existing.x, y: existing.y });
        state.selectedHighlightId = id;
        state.screen = "highlightDetail";
      } else {
        currentTrip().highlights.push(next);
        state.screen = "home";
        state.tab = "home";
      }
      state.snack = { text: existing ? "Highlight updated" : "Highlight saved", action: "View", id, kind: "highlight" };
      render();
    });
  });
  $app().querySelectorAll("[data-add-photo],[data-take-photo]").forEach((el) => {
    el.addEventListener("click", () => {
      const target = el.dataset.addPhoto || el.dataset.takePhoto;
      const swatch = [photos.pier, photos.canyon, photos.bbq, photos.cadillac][
        Math.floor(Math.random() * 4)
      ];
      if (target === "stop") state.stopForm.photos.push(swatch);
      else state.highlightForm.photos.push(swatch);
      render();
    });
  });
  $app().querySelectorAll("[data-remove-photo]").forEach((el) => {
    el.addEventListener("click", (event) => {
      event.stopPropagation();
      const [target, index] = el.dataset.removePhoto.split(":");
      if (target === "stop") state.stopForm.photos.splice(Number(index), 1);
      else state.highlightForm.photos.splice(Number(index), 1);
      render();
    });
  });
  $app().querySelectorAll("[data-dialog]").forEach((el) => {
    el.addEventListener("click", () => {
      state.dialog = el.dataset.dialog;
      render();
    });
  });
  $app().querySelectorAll("[data-edit]").forEach((el) => {
    el.addEventListener("click", () => {
      state.dialog = null;
      if (el.dataset.edit === "stop") {
        const stop = tripStops().find((s) => s.id === state.selectedStopId);
        if (!stop) return;
        state.stopForm = {
          placeName: stop.placeName,
          notes: stop.notes,
          lat: stop.lat,
          lng: stop.lng,
          photos: [stop.photo],
          dirty: false,
          editingId: stop.id,
        };
        state.screen = "addStop";
      } else {
        const highlight = tripHighlights().find((h) => h.id === state.selectedHighlightId);
        if (!highlight) return;
        state.highlightForm = {
          name: highlight.name,
          category: highlight.category,
          rating: highlight.rating,
          notes: highlight.notes,
          lat: highlight.lat,
          lng: highlight.lng,
          photos: [highlight.photo],
          dirty: false,
          editingId: highlight.id,
        };
        state.screen = "addHighlight";
      }
      render();
    });
  });
  $app().querySelectorAll("[data-dialog-cancel]").forEach((el) => {
    el.addEventListener("click", () => {
      state.dialog = null;
      render();
    });
  });
  $app().querySelectorAll("[data-discard]").forEach((el) => {
    el.addEventListener("click", () => {
      state.dialog = null;
      state.screen = "home";
      state.stopForm = blankStopForm();
      state.highlightForm = blankHighlightForm();
      render();
    });
  });
  $app().querySelectorAll("[data-confirm-delete]").forEach((el) => {
    el.addEventListener("click", () => {
      if (el.dataset.confirmDelete === "stop") {
        currentTrip().stops = currentTrip().stops.filter((s) => s.id !== state.selectedStopId);
        state.snack = { text: "Stop deleted" };
      } else {
        currentTrip().highlights = currentTrip().highlights.filter((h) => h.id !== state.selectedHighlightId);
        state.snack = { text: "Highlight deleted" };
      }
      state.dialog = null;
      state.screen = "home";
      render();
    });
  });
  $app().querySelectorAll("[data-snack-action]").forEach((el) => {
    el.addEventListener("click", () => {
      if (!state.snack?.id) return;
      if (state.snack.kind === "stop") {
        state.selectedStopId = state.snack.id;
        state.screen = "stopDetail";
      } else {
        state.selectedHighlightId = state.snack.id;
        state.screen = "highlightDetail";
      }
      state.snack = null;
      render();
    });
  });
  $app().querySelectorAll("[data-snack]").forEach((el) => {
    el.addEventListener("click", () => {
      state.snack = { text: el.dataset.snack };
      render();
    });
  });
  $app().querySelectorAll("[data-photo]").forEach((el) => {
    el.addEventListener("click", () => {
      state.photo = el.dataset.photo;
      render();
    });
  });
  $app().querySelectorAll("[data-close-photo]").forEach((el) => {
    el.addEventListener("click", () => {
      state.photo = null;
      render();
    });
  });
  const title = document.getElementById("tripTitle");
  if (title) {
    title.addEventListener("input", () => {
      if (currentTrip()) currentTrip().title = title.value;
    });
  }
  const tripNotes = document.getElementById("tripNotes");
  if (tripNotes) {
    tripNotes.addEventListener("input", () => {
      if (currentTrip()) currentTrip().notes = tripNotes.value;
    });
  }
  $app().querySelectorAll("[data-open-trip]").forEach((el) => {
    el.addEventListener("click", () => {
      state.currentTripId = el.dataset.openTrip;
      state.tab = "home";
      state.screen = "home";
      state.fabOpen = false;
      state.sheetId = null;
      render();
    });
  });
  $app().querySelectorAll("[data-back-trips]").forEach((el) => {
    el.addEventListener("click", () => {
      state.currentTripId = null;
      state.screen = "trips";
      state.fabOpen = false;
      state.sheetId = null;
      render();
    });
  });
  $app().querySelectorAll("[data-create-trip]").forEach((el) => {
    el.addEventListener("click", () => {
      state.tripForm = blankTripForm();
      state.screen = "createTrip";
      render();
    });
  });
  $app().querySelectorAll("[data-close-trip-form]").forEach((el) => {
    el.addEventListener("click", () => {
      state.screen = "trips";
      render();
    });
  });
  $app().querySelectorAll("[data-save-trip]").forEach((el) => {
    el.addEventListener("click", () => {
      const titleValue = state.tripForm.title.trim();
      if (!titleValue) return;
      if (state.tripForm.editingId) {
        const trip = state.trips.find((t) => t.id === state.tripForm.editingId);
        if (trip) {
          trip.title = titleValue;
          trip.startDate = state.tripForm.startDate;
          trip.endDate = state.tripForm.endDate || null;
          trip.notes = state.tripForm.notes;
        }
        state.screen = "trips";
        state.snack = { text: "Trip updated" };
      } else {
        const id = `t${Date.now()}`;
        state.trips.push({
          id,
          title: titleValue,
          startDate: state.tripForm.startDate,
          endDate: state.tripForm.endDate || null,
          notes: state.tripForm.notes,
          stops: [],
          highlights: [],
        });
        state.currentTripId = id;
        state.tab = "home";
        state.screen = "home";
        state.snack = { text: "Trip started" };
      }
      render();
    });
  });
  const newTripTitle = document.getElementById("newTripTitle");
  if (newTripTitle) {
    newTripTitle.addEventListener("input", () => {
      state.tripForm.title = newTripTitle.value;
      state.tripForm.dirty = true;
      renderKeepTripTitle();
    });
  }
  const newTripStart = document.getElementById("newTripStart");
  if (newTripStart) {
    newTripStart.addEventListener("change", () => {
      state.tripForm.startDate = newTripStart.value;
    });
  }
  const newTripEnd = document.getElementById("newTripEnd");
  if (newTripEnd) {
    newTripEnd.addEventListener("change", () => {
      state.tripForm.endDate = newTripEnd.value;
    });
  }
  const newTripNotes = document.getElementById("newTripNotes");
  if (newTripNotes) {
    newTripNotes.addEventListener("input", () => {
      state.tripForm.notes = newTripNotes.value;
    });
  }
  const tripSearch = document.getElementById("tripSearch");
  if (tripSearch) {
    tripSearch.addEventListener("input", () => {
      state.tripQuery = tripSearch.value;
      render();
      const again = document.getElementById("tripSearch");
      if (again) {
        again.focus();
        again.setSelectionRange(again.value.length, again.value.length);
      }
    });
  }
  $app().querySelectorAll("[data-units]").forEach((el) => {
    el.addEventListener("click", () => {
      state.units = el.dataset.units;
      render();
    });
  });
  $app().querySelectorAll("[data-trip-overflow]").forEach((el) => {
    el.addEventListener("click", (event) => {
      event.stopPropagation();
      state.pendingDeleteTripId = el.dataset.tripOverflow;
      state.dialog = "trip-overflow";
      render();
    });
  });
  $app().querySelectorAll("[data-edit-trip]").forEach((el) => {
    el.addEventListener("click", () => {
      const trip = state.trips.find((t) => t.id === state.pendingDeleteTripId);
      state.dialog = null;
      if (!trip) return;
      state.tripForm = {
        title: trip.title,
        startDate: trip.startDate,
        endDate: trip.endDate || "",
        notes: trip.notes,
        dirty: false,
        editingId: trip.id,
      };
      state.screen = "createTrip";
      render();
    });
  });
  $app().querySelectorAll("[data-confirm-delete-trip]").forEach((el) => {
    el.addEventListener("click", () => {
      state.trips = state.trips.filter((t) => t.id !== state.pendingDeleteTripId);
      if (state.currentTripId === state.pendingDeleteTripId) state.currentTripId = null;
      state.pendingDeleteTripId = null;
      state.dialog = null;
      state.screen = "trips";
      state.snack = { text: "Trip deleted" };
      render();
    });
  });
  $app().querySelectorAll("[data-directions]").forEach((el) => {
    el.addEventListener("click", () => {
      const [lat, lng] = el.dataset.directions.split(",");
      state.snack = { text: `Google Maps would open ${lat}, ${lng}` };
      render();
    });
  });
}

function applyCurrentLocation() {
  const loc = { lat: 36.1627, lng: -86.7816 };
  if (state.screen === "addStop") {
    state.stopForm.lat = loc.lat;
    state.stopForm.lng = loc.lng;
    if (!state.stopForm.placeName) state.stopForm.placeName = "Current location";
    state.stopForm.dirty = true;
  } else if (state.screen === "addHighlight") {
    state.highlightForm.lat = loc.lat;
    state.highlightForm.lng = loc.lng;
    state.highlightForm.dirty = true;
  } else {
    state.snack = { text: "Centered on current location" };
  }
  render();
}

function renderKeepTripTitle() {
  const title = state.tripForm.title;
  const notes = state.tripForm.notes;
  render();
  const n = document.getElementById("newTripTitle");
  const t = document.getElementById("newTripNotes");
  if (n) {
    n.value = title;
    n.focus();
    n.setSelectionRange(title.length, title.length);
  }
  if (t) t.value = notes;
}

function renderKeepHighlight() {
  const name = state.highlightForm.name;
  const notes = state.highlightForm.notes;
  render();
  const n = document.getElementById("hlName");
  const t = document.getElementById("hlNotes");
  if (n) {
    n.value = name;
    n.focus();
    n.setSelectionRange(name.length, name.length);
  }
  if (t) t.value = notes;
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;");
}

function tickClock() {
  const now = new Date();
  document.getElementById("clock").textContent = now.toLocaleTimeString(undefined, {
    hour: "numeric",
    minute: "2-digit",
  });
}

document.getElementById("toggleTheme").addEventListener("click", () => {
  state.theme = state.theme === "dark" ? "light" : "dark";
  render();
});

document.getElementById("toggleEmpty").addEventListener("click", () => {
  state.emptyList = !state.emptyList;
  state.trips = state.emptyList ? [] : createSeedTrips();
  state.currentTripId = null;
  state.screen = "trips";
  state.tab = "home";
  state.fabOpen = false;
  state.snack = null;
  state.sheetId = null;
  state.dialog = null;
  render();
});

document.getElementById("resetProto").addEventListener("click", () => {
  state.theme = "light";
  state.emptyList = false;
  state.units = "mi";
  state.tab = "home";
  state.screen = "trips";
  state.fabOpen = false;
  state.mapFilter = "All";
  state.timelineFilter = "All";
  state.timelineQuery = "";
  state.tripQuery = "";
  state.dialog = null;
  state.snack = null;
  state.photo = null;
  state.sheetId = null;
  state.locationGranted = false;
  state.currentTripId = null;
  state.pendingDeleteTripId = null;
  state.trips = createSeedTrips();
  state.stopForm = blankStopForm();
  state.highlightForm = blankHighlightForm();
  state.tripForm = blankTripForm();
  render();
});

tickClock();
window.setInterval(tickClock, 30000);
render();
