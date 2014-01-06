$(document).ready(function() {
    var colors = ["#69D2E7", "#A7DBD8", "#E0E4CC", "#F38630", "#FA6900"];
    var data = [];
    for (var i = 0; i < 2; i++) {
        data[i] = {"value": (i + 1) * 10, "color": colors[i % colors.length]}
    }
    new Chart($("#chart").get(0).getContext("2d")).Pie(data);
})