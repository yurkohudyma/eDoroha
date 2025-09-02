const map = L.map('map').setView([48.919293, 24.712843], 17);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; OSM contributors'
}).addTo(map);

let startMarker = null;
let endMarker = null;
let routeLine = null;

// Колекція всіх маршрутів, які вже є на карті
const savedRoutes = [];

map.on('click', function (e) {
  if (!startMarker) {
    startMarker = L.marker(e.latlng).addTo(map).bindPopup("Старт").openPopup();
  } else if (!endMarker) {
    endMarker = L.marker(e.latlng).addTo(map).bindPopup("Фініш").openPopup();
    fetchRoute(startMarker.getLatLng(), endMarker.getLatLng());
  } else {
    map.removeLayer(startMarker);
    map.removeLayer(endMarker);
    if (routeLine) map.removeLayer(routeLine);
    startMarker = endMarker = null;
  }
});

// --- Нове: завантажити всі збережені маршрути при старті
window.onload = () => {
  loadSavedRoutes();
};

async function loadSavedRoutes() {
  try {
    const response = await fetch("http://localhost:8080");
    if (!response.ok) {
      throw new Error(`Failed to load saved routes: ${response.status}`);
    }

    const routes = await response.json();
    routes.forEach(drawSavedRoute);
  } catch (error) {
    console.error("Error loading saved routes:", error);
  }
}

function drawSavedRoute(route) {
  if (!route.routeList || route.routeList.length < 2) return;

  const coords = route.routeList.map(pair => [pair[0], pair[1]]);
  const score = getScoreFromGrade(route.gradeStatus);
  const polyline = L.polyline(coords, {
    color: getColorByScore(score),
    weight: 5
  }).addTo(map);

  savedRoutes.push(polyline);
}

async function fetchRoute(start, end) {
  const url = 'http://localhost:8080';

  const body = {
    departure: {
      latitude: start.lat,
      longitude: start.lng
    },
    destination: {
      latitude: end.lat,
      longitude: end.lng
    }
  };

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();

    if (routeLine) {
      map.removeLayer(routeLine);
    }

    const coords = data.routePoints.map(coord => [coord[0], coord[1]]);

    routeLine = L.polyline(coords, { color: 'blue', weight: 5 }).addTo(map);
    map.fitBounds(routeLine.getBounds());

    showRatingPopup(coords);

  } catch (err) {
    console.error("Routing error:", err);
  }
}

function showRatingPopup(routeList) {
  const popupDiv = L.DomUtil.create('div', 'rating-popup');
  popupDiv.innerHTML = "<b>Оцініть якість покриття:</b><br>";
  const colors = ['black', 'brown', 'orange', 'yellow', 'green'];
  const gradeEnumValues = ['AWFUL', 'BAD', 'FAIR', 'GOOD', 'EXCELLENT'];

  colors.forEach((color, index) => {
    const btn = document.createElement('button');
    btn.innerText = `${index + 1}`;
    btn.className = 'rating-btn';
    btn.style.backgroundColor = color;
    btn.onclick = () => {
      const gradeStatus = gradeEnumValues[index];
      saveRating(gradeStatus, routeList, index + 1);
      map.closePopup(); // ✅ Закрити попап після вибору
    };
    popupDiv.appendChild(btn);
  });

  L.popup()
    .setLatLng(routeLine.getCenter())
    .setContent(popupDiv)
    .openOn(map);
}

function saveRating(gradeStatus, routeList, score) {
  const payload = {
    userId: 1,
    gradeStatus: gradeStatus,
    routeList: routeList
  };

  console.log("Payload to backend:", JSON.stringify(payload, null, 2)); // DEBUG

  fetch('/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (res.ok) {
        alert('Оцінку збережено!');
        routeLine.setStyle({ color: getColorByScore(score) });

        // 🆕 Оновити карту — додати новий маршрут
        drawSavedRoute({
          gradeStatus,
          routeList
        });

      } else {
        alert('Помилка при збереженні');
      }
    })
    .catch(err => console.error("POST error:", err));
}

function getColorByScore(score) {
  return {
    1: 'black',
    2: 'brown',
    3: 'orange',
    4: 'yellow',
    5: 'green'
  }[score] || 'gray';
}

function getScoreFromGrade(grade) {
  switch (grade) {
    case 'AWFUL': return 1;
    case 'BAD': return 2;
    case 'FAIR': return 3;
    case 'GOOD': return 4;
    case 'EXCELLENT': return 5;
    default: return 0;
  }
}
