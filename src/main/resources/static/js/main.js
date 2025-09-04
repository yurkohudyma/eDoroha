const map = L.map('map').setView([48.919293, 24.712843], 17);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; OSM contributors'
}).addTo(map);

let startMarker = null;
let endMarker = null;
let routeLine = null;
const savedRoutes = [];

map.on('click', function (e) {
  if (!startMarker) {
    startMarker = L.marker(e.latlng).addTo(map).bindPopup("Старт").openPopup();
  } else if (!endMarker) {
    endMarker = L.marker(e.latlng).addTo(map).bindPopup("Фініш").openPopup();
    fetchRoute(startMarker.getLatLng(), endMarker.getLatLng());
  } else {
    clearTempRoute();
  }
});

function clearTempRoute() {
  if (startMarker) map.removeLayer(startMarker);
  if (endMarker) map.removeLayer(endMarker);
  if (routeLine) map.removeLayer(routeLine);
  startMarker = endMarker = routeLine = null;
}

window.onload = () => {
  loadSavedRoutes();
};

async function loadSavedRoutes() {
  try {
    const response = await fetch("http://localhost:8080");
    if (!response.ok) throw new Error(`Failed to load saved routes: ${response.status}`);

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
    weight: 5,
    className: 'route-line'
  }).addTo(map);

  polyline.on('click', (e) => {
    L.DomEvent.stopPropagation(e);
    showEditGradePopup(polyline, route);
  });

  polyline.on('mouseover', () => {
    polyline.setStyle({ weight: 7, opacity: 0.9 });
    polyline.bindTooltip("Редагувати").openTooltip();
  });

  polyline.on('mouseout', () => {
    polyline.setStyle({ weight: 5, opacity: 1 });
    polyline.closeTooltip();
  });

  savedRoutes.push(polyline);
}

async function fetchRoute(start, end) {
  const url = 'http://localhost:8080';
  const body = {
    departure: { latitude: start.lat, longitude: start.lng },
    destination: { latitude: end.lat, longitude: end.lng }
  };

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

    const data = await response.json();
    const coords = data.routePoints.map(coord => [coord[0], coord[1]]);

    if (routeLine) map.removeLayer(routeLine);
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
      map.closePopup();
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

  fetch('/save', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
    .then(async res => {
      if (!res.ok) {
        alert('Помилка при збереженні');
        return;
      }

      const saved = await res.json();

      alert('Оцінку збережено!');

      routeLine.setStyle({ color: getColorByScore(score) });

      drawSavedRoute({
        gradeId: saved.gradeId,
        gradeStatus: saved.gradeStatus,
        routeList: saved.routeList
      });
    })
    .catch(err => console.error("POST error:", err));
}

function showEditGradePopup(polyline, routeData) {
  const popupDiv = L.DomUtil.create('div', 'edit-popup');
  popupDiv.innerHTML = "<b>Оберіть нову оцінку покриття:</b><br>";
  const colors = ['black', 'brown', 'orange', 'yellow', 'green'];
  const gradeEnumValues = ['AWFUL', 'BAD', 'FAIR', 'GOOD', 'EXCELLENT'];

  colors.forEach((color, index) => {
    const btn = document.createElement('button');
    btn.innerText = `${index + 1}`;
    btn.className = 'rating-btn';
    btn.style.backgroundColor = color;
    btn.onclick = () => {
      const newGrade = gradeEnumValues[index];
      patchRouteGrade(routeData.gradeId, newGrade, polyline, index + 1);
      map.closePopup();
    };
    popupDiv.appendChild(btn);
  });

  L.popup()
    .setLatLng(polyline.getCenter())
    .setContent(popupDiv)
    .openOn(map);
}

function patchRouteGrade(gradeId, newGradeStatus, polyline, score) {
  if (!gradeId) {
    alert('Неможливо змінити оцінку — відсутній gradeId!');
    console.error("❌ PATCH error: gradeId is null or undefined");
    return;
  }

  const payload = {
    gradeId: gradeId,
    newGradeStatus: newGradeStatus
  };

  fetch('http://localhost:8080', {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (res.ok) {
        alert('Оцінку оновлено!');
        polyline.setStyle({ color: getColorByScore(score) });
      } else {
        alert('Помилка при оновленні оцінки');
      }
    })
    .catch(err => console.error("PATCH error:", err));
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