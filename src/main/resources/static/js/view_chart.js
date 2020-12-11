function viewChart(currTag, tag2, tag3, e1, e2, e3){
  document.getElementById(currTag).style.backgroundColor = "#94b1da";
  document.getElementById(tag2).style.backgroundColor = "#2b2b2b";
  document.getElementById(tag3).style.backgroundColor = "#2b2b2b";
  document.getElementById(e1).style.display = 'block';
  document.getElementById(e2).style.display = 'none';
  document.getElementById(e3).style.display = 'none';
}

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawDayChart);
google.setOnLoadCallback(drawWeekChart);
google.setOnLoadCallback(drawMonthChart);

function drawDayChart() {
    var data = google.visualization.arrayToDataTable([
     ['Газ', 'Объём'],
     ['Азот',     78.09],
     ['Кислород', 20.95],
     ['Аргон',    0.93],
     ['Углекислый газ', 0.03]
    ]);
    var options = {
     title: 'Состав воздуха',
     is3D: true,
     pieResidueSliceLabel: 'Остальное'
    };
    var chart = new google.visualization.PieChart(document.getElementsByClassName('dayChart')[0]);
    chart.draw(data, options);
}

function drawWeekChart() {
    var data = google.visualization.arrayToDataTable([
        ['Газ', 'Объём'],
        ['Азот',     78.09],
        ['Кислород', 20.95],
        ['Аргон',    0.93],
        ['Углекислый газ', 0.03]
    ]);
    var options = {
        title: 'Состав воздуха',
        is3D: true,
        pieResidueSliceLabel: 'Остальное'
    };
    var chart = new google.visualization.PieChart(document.getElementsByClassName('weekChart')[0]);
    chart.draw(data, options);
}

function drawMonthChart() {
    var data = google.visualization.arrayToDataTable([
        ['Газ', 'Объём'],
        ['Азот',     78.09],
        ['Кислород', 20.95],
        ['Аргон',    0.93],
        ['Углекислый газ', 0.03]
    ]);
    var options = {
        title: 'Состав воздуха',
        is3D: true,
        pieResidueSliceLabel: 'Остальное'
    };
    var chart = new google.visualization.PieChart(document.getElementsByClassName('monthChart')[0]);
    chart.draw(data, options);
}