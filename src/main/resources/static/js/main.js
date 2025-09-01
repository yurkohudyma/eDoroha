const map = L.map('map').setView([48.919293, 24.712843], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; OSM contributors'
}).addTo(map);

let startMarker = null;
let endMarker = null;
let routeLine = null;

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

/*function fetchRoute(start, end) {
  const url = `http://localhost:8989/route?point=${start.lat},${start.lng}&point=${end.lat},${end.lng}&type=json&vehicle=car&locale=uk&points_encoded=false`;

  fetch(url)
    .then(res => res.json())
    .then(data => {
      const coords = data.paths[0].points.coordinates.map(c => [c[1], c[0]]);
      routeLine = L.polyline(coords, { color: 'blue', weight: 5 }).addTo(map);
      map.fitBounds(routeLine.getBounds());
      showRatingPopup(coords);
    })
    .catch(err => console.error("Routing error:", err));
}*/

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

    // Видаляємо старий маршрут, якщо він є
    if (routeLine) {
      map.removeLayer(routeLine);
    }

    // Тепер routePoints — це масив [latitude, longitude]
    // Leaflet очікує [lat, lng] — порядок вірний, просто передаємо напряму
    const coords = data.routePoints.map(coord => [coord[0], coord[1]]);

    routeLine = L.polyline(coords, { color: 'blue', weight: 5 }).addTo(map);
    map.fitBounds(routeLine.getBounds());

    showRatingPopup(coords);

  } catch (err) {
    console.error("Routing error:", err);
  }
}


function showRatingPopup(coords) {
  const popupDiv = L.DomUtil.create('div', 'rating-popup');
  popupDiv.innerHTML = "<b>Оцініть якість покриття:</b><br>";
  const colors = ['black', 'brown', 'orange', 'yellow', 'green'];

  colors.forEach((color, index) => {
    const btn = document.createElement('button');
    btn.innerText = `${index + 1}`;
    btn.className = 'rating-btn';
    btn.style.backgroundColor = color;
    btn.onclick = () => {
      saveRating(index + 1, coords);
    };
    popupDiv.appendChild(btn);
  });

  L.popup()
    .setLatLng(routeLine.getCenter())
    .setContent(popupDiv)
    .openOn(map);
}

function saveRating(score, coords) {
  fetch('/api/route-feedback', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      score: score,
      geometry: coords
    })
  })
  .then(res => {
    if (res.ok) {
      alert('Оцінку збережено!');
      routeLine.setStyle({ color: getColorByScore(score) });
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
