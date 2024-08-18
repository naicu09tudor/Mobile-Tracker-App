/*
Conține funcții generale utilizate în aplicație.
sendRequest efectuează solicitări AJAX la server și gestionează răspunsurile.
goToPage și goToPage2 sunt folosite pentru a redirecționa utilizatorul către alte pagini web, cu sau fără parametri suplimentari.
*/

function sendRequest(type, resource, data, token, successHandler, errHandler) {
    $.ajax({
        type: type,
        url: "http://localhost:8082/" + resource,
        data: data,
        dataType: "json",
        accepts: "application/json",
        contentType:"application/json",

        headers:{"Authorization" : "Bearer " + token},

        success: function (data, status, jqXHR) {
            successHandler(data);
        },

        error: function (jqXHR, status) {
            alert("Please login before proceeding.");
        }
    });
}

function goToPage(url) {
    let deviceId = $('#deviceId').val().trim();
    let startDate = $('#startDate').val().trim();
    let endDate = $('#endDate').val().trim();

    let params = `?deviceId=${deviceId}&startDate=${startDate}&endDate=${endDate}`;
    $(location).attr('href', `${url}${params}`);
}

function goToPage2(url) {
    $(location).attr('href', url);
}