/*
Se ocupă cu operațiunile asupra terminalelor, cum ar fi crearea și ștergerea terminalelor.
createTerminal și deleteTerminal trimit solicitări la server pentru a efectua aceste acțiuni.
Criteria este folosit pentru a colecta datele de intrare pentru operațiunile asupra terminalelor.
 */

function createTerminal(terminalData, successCallback, errorCallback) {
    criteria = new Criteria();
    const token = localStorage.getItem("token");
    terminalData = criteria;
    sendRequest("POST", "terminal", JSON.stringify(terminalData), token, getPositionsSuccessHandler, getPositionsErrorHandler);
}

function deleteTerminal(terminalId, successCallback, errorCallback) {
    criteria = new Criteria();
    const token = localStorage.getItem("token");
    const url = `terminal?id=${criteria.id}`;
    sendRequest("DELETE", url, null, token, successCallback, errorCallback);
}

function Criteria() {
    let id = $('#id').val().trim(); // select data from input and trim it
    if (id.length > 0) {
        this.id = id;
    }

    let terminalName = $('#terminalName').val().trim(); // select data from input and trim it
    if (terminalName.length > 0) {
        this.terminalName = terminalName;
    }
}

function getPositionsSuccessHandler(respData) {
    $("#result").append("<br>" + JSON.stringify(respData));
}

function getPositionsErrorHandler(status) {
    alert("err response: " + status); // popup on err.
}