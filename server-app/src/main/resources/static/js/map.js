/*
Se ocupă cu inițializarea și gestionarea hărții folosind OpenLayers.
Include funcții precum initializeMap pentru crearea hărții și adăugarea de markeri,
addStaticMarker pentru adăugarea unor markeri fixi, și getRandomPosition pentru generarea unor poziții aleatorii.
 */


let map;
let markers;
let myLatLng = {lat: 46.7693924, lng: 23.5902006};

function initializeMap() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const deviceId = urlParams.get('deviceId');
    const startDate = urlParams.get('startDate');
    const endDate = urlParams.get('endDate');

    if (deviceId && startDate && endDate) {
        let criteria2 = new Criteria2(deviceId, startDate, endDate);
        fetchPositions(criteria2, (fetchedData) => {
            map = new OpenLayers.Map("mapdiv");
            map.addLayer(new OpenLayers.Layer.OSM());
            let zoom = 10;

            if (fetchedData.length === 0) {
                let lonLat = getLonLat(myLatLng.lat, myLatLng.lng);
                markers = new OpenLayers.Layer.Markers("Markers");
                map.addLayer(markers);
                markers.addMarker(new OpenLayers.Marker(lonLat));
                map.setCenter(lonLat, zoom);
            } else {
                fetchedData.forEach(position => {
                    let lonLat = getLonLat(position.latitude, position.longitude);
                    markers = new OpenLayers.Layer.Markers("Markers");
                    map.addLayer(markers);
                    markers.addMarker(new OpenLayers.Marker(lonLat));
                    map.setCenter(lonLat, zoom);
                });
            }
        });
    } else {
        alert("Please enter valid data.");
    }
}

function addStaticMarker() {
    let pos = getRandomPosition();
    let lonLat = getLonLat(pos.lat, pos.lng);
    markers.addMarker(new OpenLayers.Marker(lonLat));
}

function getRandomPosition() {
    let randLatLng = {
        lat: (myLatLng["lat"] + Math.floor(Math.random() * 5) + 1),
        lng: (myLatLng["lng"] + Math.floor(Math.random() * 5) + 1)
    };
    return randLatLng;
}

function getLonLat(lat, lng) {
    return new OpenLayers.LonLat(lng, lat)
        .transform(
            new OpenLayers.Projection("EPSG:4326"),
            map.getProjectionObject()
        );
}