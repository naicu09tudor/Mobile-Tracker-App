/*
Conține logica pentru obținerea și afișarea datelor de poziție.
getPositions și fetchPositions solicită date de poziție de la server.
Criteria și Criteria2 sunt folosite pentru a crea obiecte de criterii bazate pe intrările utilizatorului.
getPositionsSuccessHandler și getPositionsErrorHandler gestionează răspunsurile la solicitări.
 */

let fetchedData;

function getPositions() {
    let criteria = new Criteria();

    let token = localStorage.getItem("token");

    sendRequest("GET", "positions?" + $.param(criteria), null, token, getPositionsSuccessHandler, getPositionsErrorHandler);
}

function fetchPositions(criteria2, callback) {
    let token = localStorage.getItem("token");

    sendRequest("GET", "positions?" + $.param(criteria2), null, token,
        (respData) => {
            callback(respData);
        },
        getPositionsErrorHandler
    );
}

function Criteria2(deviceId, startDate, endDate) {
    this.terminalId = deviceId;
    this.startDate = startDate;
    this.endDate = endDate;
}

function Criteria() {
    let deviceId = $('#deviceId').val().trim(); // select data from input and trim it
    if (deviceId.length > 0) {
        this.terminalId = deviceId;
    }

    let startDate = $('#startDate').val().trim(); // select data from input and trim it
    if (startDate.length > 0) {
        this.startDate = startDate;
    }

    let endDate = $('#endDate').val().trim(); // select data from input and trim it
    if (endDate.length > 0) {
        this.endDate = endDate;
    }
}

function getPositionsSuccessHandler(respData) {
    let resultsContainer = $("#result");
    resultsContainer.empty(); // Clear previous results

    // Check if respData is not empty
    if(respData && respData.length > 0) {
        // Create a container for the positions
        let list = $("<div></div>");

        // Iterate over each position and create a styled block
        respData.forEach(function(position) {
            let positionDiv = $("<div class='position-item'></div>");
            positionDiv.append("<p>ID: " + position.id + "</p>");
            positionDiv.append("<p>Latitude: " + position.latitude + "</p>");
            positionDiv.append("<p>Longitude: " + position.longitude + "</p>");
            positionDiv.append("<p>Terminal ID: " + position.terminalId + "</p>");
            positionDiv.append("<p>Creation Date: " + position.creationDate + "</p>");
            list.append(positionDiv);
        });
        resultsContainer.append(list);
    } else {
        resultsContainer.append("<p>No positions to display.</p>");
    }
}


function getPositionsErrorHandler(status) {
    alert("err response: " + status); // popup on err.
}
